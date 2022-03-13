package io.sarvika.clowre.docserver;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;

public class ViewNotEnabledException extends HttpStatusException {

    public ViewNotEnabledException() {
        super(HttpStatus.NOT_FOUND, "this view is not enabled");
    }
}
