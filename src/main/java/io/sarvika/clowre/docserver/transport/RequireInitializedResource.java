package io.sarvika.clowre.docserver.transport;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.filter.FilterChain;
import io.micronaut.http.filter.HttpFilter;
import io.sarvika.clowre.docserver.resources.SwaggerResource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

@Singleton
@Filter({"/docs/**", "/views/**"})
public class RequireInitializedResource implements HttpFilter {

    @Inject
    private SwaggerResource resource;


    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(HttpRequest<?> request, FilterChain chain) {

        if (resource.read() == null) {
            throw new HttpStatusException(HttpStatus.EXPECTATION_FAILED, "document resource not initialized yet");
        }

        return chain.proceed(request);
    }
}
