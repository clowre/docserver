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

    private Long refreshDelay = (long) (60 * 5);

    public List<SwaggerSource> getSources() {
        return sources;
    }

    public void setSources(List<SwaggerSource> sources) {
        this.sources = sources;
    }

    public Long getRefreshDelay() {
        return refreshDelay;
    }

    public void setRefreshDelay(Long refreshDelay) {
        this.refreshDelay = refreshDelay;
    }
}
