package io.sarvika.clowre.docserver.configuration.oas;

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.models.servers.Server;

@Introspected
@EachProperty("swagger.servers")
public class ServerConfiguration extends Server {
}
