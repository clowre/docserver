package io.sarvika.clowre.docserver.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;

@ConfigurationProperties(value = "swagger.info")
public class InfoConfiguration {

    @NotEmpty
    private String title;

    private String description;
    private String version;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if (StringUtils.isNotBlank(description)) {
            return description;
        }

        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
