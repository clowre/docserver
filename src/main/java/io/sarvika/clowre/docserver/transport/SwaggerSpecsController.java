package io.sarvika.clowre.docserver.transport;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.sarvika.clowre.docserver.resources.SwaggerResource;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.inject.Inject;

@Controller
public class SwaggerSpecsController {

    @Inject
    private SwaggerResource swaggerResource;

    @Get("/docs.json")
    OpenAPI getDocsJOSN() {
        return swaggerResource.read();
    }

    @Get("/docs.yaml")
    @Produces(MediaType.APPLICATION_YAML)
    OpenAPI getDocsYAML() {
        return swaggerResource.read();
    }
}
