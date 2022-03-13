package io.sarvika.clowre.docserver.transport;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import io.sarvika.clowre.docserver.ViewNotEnabledException;
import io.sarvika.clowre.docserver.configuration.InfoConfiguration;
import io.sarvika.clowre.docserver.configuration.ViewConfiguration;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;

@Controller("/views")
public class ViewsController {

    @Inject
    private ViewConfiguration viewConfiguration;

    @Inject
    private InfoConfiguration infoConfiguration;

    @Get("/redoc")
    @View("redoc")
    ViewModel renderRedocView() {
        if (!viewConfiguration.isEnableRedoc()) {
            throw new ViewNotEnabledException();
        }

        return vm();
    }

    private ViewModel vm() {
        final var model = new ViewModel();
        model.setTitle(infoConfiguration.getTitle());
        model.setDescription(infoConfiguration.getDescription());
        return model;
    }
}
