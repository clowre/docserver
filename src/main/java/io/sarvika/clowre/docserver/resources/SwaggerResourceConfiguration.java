package io.sarvika.clowre.docserver.resources;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * This class exists as a workaround to start reading swagger resources as soon as application starts up.
 */
@Singleton
class SwaggerResourceConfiguration implements ApplicationEventListener<ServerStartupEvent> {

    @Inject
    SwaggerResource swaggerResource;

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        swaggerResource.read();
    }
}
