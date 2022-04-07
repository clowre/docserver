package io.sarvika.clowre.docserver.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import io.sarvika.clowre.docserver.configuration.oas.ServerConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Introspected
@ConfigurationProperties(value = "swagger")
public class SwaggerSourcesConfiguration {

    @NotEmpty
    private List<@Valid SwaggerSource> sources;

    private List<@Valid ServerConfiguration> servers;

    private Long refreshDelay = (long) (60 * 5);

    public List<SwaggerSource> getSources() {
        return sources;
    }

    public void setSources(List<SwaggerSource> sources) {
        this.sources = sources;
    }

    public List<ServerConfiguration> getServers() {
        return servers;
    }

    public void setServers(List<ServerConfiguration> servers) {
        this.servers = servers;
    }

    public Long getRefreshDelay() {
        return refreshDelay;
    }

    public void setRefreshDelay(Long refreshDelay) {
        this.refreshDelay = refreshDelay;
    }
}
