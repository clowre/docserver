package io.sarvika.clowre.docserver.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Introspected
@ConfigurationProperties(value = "swagger")
public class SwaggerSourcesConfiguration {

    @NotEmpty
    private List<@Valid SwaggerSource> sources;

    public List<SwaggerSource> getSources() {
        return sources;
    }

    public void setSources(List<SwaggerSource> sources) {
        this.sources = sources;
    }
}
