package io.sarvika.clowre.docserver.resources;

import io.sarvika.clowre.docserver.configuration.InfoConfiguration;
import io.sarvika.clowre.docserver.configuration.SwaggerSource;
import io.sarvika.clowre.docserver.configuration.SwaggerSourcesConfiguration;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import joptsimple.internal.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@Singleton
public class ConsolidatedSwaggerResource implements SwaggerResource {

    private static final Logger logger = LoggerFactory.getLogger(ConsolidatedSwaggerResource.class);
    private static final Object lock = new Object();

    @Inject
    private SwaggerSourcesConfiguration sourcesConfiguration;

    @Inject
    private InfoConfiguration infoConfiguration;

    private Date nextReadAfter;
    private OpenAPI openAPI = new OpenAPI();

    @Override
    public OpenAPI read() {

        if (this.readTimeElapsed()) {
            this.readFromSource();
        }

        return openAPI;
    }

    private boolean readTimeElapsed() {
        if (this.nextReadAfter == null) {
            return true;
        }

        return new Date().after(this.nextReadAfter);
    }

    private void readFromSource() {

        final var parseOptions = new ParseOptions();
        parseOptions.setResolveFully(true);

        OpenAPI openAPI = null;
        for (final var source : sourcesConfiguration.getSources()) {
            logger.debug("preparing to read source \"" + source.getName() + "\" from " + source.getAddress());

            SwaggerParseResult parseResult;
            switch (source.getVersion()) {
                case V2:
                    parseResult = new OpenAPIParser().readLocation(source.getAddress(), null, parseOptions);
                    break;
                case V3:
                    parseResult = new OpenAPIV3Parser().readLocation(source.getAddress(), null, parseOptions);
                    break;
                default:
                    logger.error("unknown schema version " + source.getVersion());
                    continue;
            }

            final var errors = parseResult.getMessages();
            if (errors != null) {
                errors.forEach(e -> logger.warn("parse \"" + source.getName() + "\": " + e));
            }

            if (parseResult.getOpenAPI() == null) {
                logger.error("source \"" + source.getName() + "\" was not parsed");
                continue;
            }

            openAPI = this.mergeParseResult(source, parseResult, openAPI);
        }

        if (openAPI != null) {

            openAPI.setInfo(
                    new Info()
                            .title(infoConfiguration.getTitle())
                            .description(infoConfiguration.getDescription())
                            .version(infoConfiguration.getVersion())
            );

            synchronized (lock) {
                this.openAPI = openAPI;

                final var ci = Calendar.getInstance();
                ci.add(Calendar.MINUTE, 5);
                this.nextReadAfter = ci.getTime();
            }
        }
    }

    private OpenAPI mergeParseResult(SwaggerSource source, SwaggerParseResult parseResult, OpenAPI dest) {
        if (dest == null) {
            dest = new OpenAPI();
        }

        final var sourceOpenAPI = parseResult.getOpenAPI();
        if (sourceOpenAPI.getComponents() != null) {
            mergeComponents(source, sourceOpenAPI.getComponents(), dest);
        }

        if (sourceOpenAPI.getPaths() != null) {
            mergePaths(source, sourceOpenAPI.getPaths(), dest);
        }

        return dest;
    }

    private void mergePaths(SwaggerSource source, Paths paths, OpenAPI dest) {

        if (dest.getPaths() == null) {
            dest.setPaths(new Paths());
        }

        paths.forEach((k, v) -> {
            this.updatePathItem(source, v);
            dest.getPaths().addPathItem("/" + source.getPathPrefix() + k, v);
        });
    }

    private void mergeComponents(SwaggerSource source, Components components, OpenAPI dest) {
        if (dest.getComponents() == null) {
            dest.setComponents(new Components());
        }

        final var destComponents = dest.getComponents();
        if (components.getSchemas() != null) {
            components.getSchemas().forEach((k, v) -> {
                this.updateSchemaRef(source, v);
                destComponents.addSchemas(source.getName() + "." + k, v);
            });
        }

        if (components.getRequestBodies() != null) {
            components.getRequestBodies().forEach((k, v) -> {
                this.updateRequestBodies(source, v);
                destComponents.addRequestBodies(source.getName() + "." + k, v);
            });
        }

        if (components.getCallbacks() != null) {
            components.getCallbacks().forEach(dest.getComponents()::addCallbacks);
        }

    }

    private void updatePathItem(SwaggerSource source, PathItem pi) {

        for (final var op : pi.readOperations()) {
            if (op.getParameters() != null) {
                op.getParameters().forEach(p -> this.updateParameter(source, p));
            }

            if (op.getTags() == null) {
                op.addTagsItem(source.getName());
            } else {
                op.setTags(
                        op.getTags()
                                .stream()
                                .map(t -> source.getName() + "/" + t)
                                .collect(Collectors.toList())
                );
            }

            op.setOperationId(source.getName() + "/" + op.getOperationId());

            final var apiResponses = op.getResponses().values();
            apiResponses.forEach(res -> this.updateContent(source, res.getContent()));
        }

        if (pi.getParameters() != null) {
            pi.getParameters().forEach(p -> this.updateParameter(source, p));
        }
    }

    private void updateParameter(SwaggerSource source, Parameter parameter) {
        this.updateSchemaRef(source, parameter.getSchema());
    }

    private void updateRequestBodies(SwaggerSource source, RequestBody body) {
        this.updateContent(source, body.getContent());
    }

    private void updateContent(SwaggerSource source, Content content) {
        if (content == null) {
            return;
        }

        content.values().forEach(s -> this.updateSchemaRef(source, s.getSchema()));
    }

    private void updateSchemaRef(SwaggerSource source, Schema<?> s) {
        if (s == null) {
            return;
        }

        final var ref = s.get$ref();
        if (StringUtils.isNotBlank(ref)) {
            final var refs = ref.split("/");
            refs[refs.length - 1] = source.getName() + "." + refs[refs.length - 1];

            s.set$ref(Strings.join(refs, "/"));
        }

        if (s instanceof ArraySchema) {
            final var as = (ArraySchema) s;
            this.updateSchemaRef(source, as.getItems());
        }

        if (s.getProperties() != null) {
            for (final var propertySchema : s.getProperties().values()) {
                this.updateSchemaRef(source, propertySchema);
            }
        }
    }
}
