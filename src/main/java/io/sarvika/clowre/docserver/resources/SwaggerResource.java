package io.sarvika.clowre.docserver.resources;

import io.swagger.v3.oas.models.OpenAPI;

public interface SwaggerResource {
    OpenAPI read();
}
