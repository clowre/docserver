package io.sarvika.clowre.docserver.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("views")
public class ViewConfiguration {

    private boolean enableRedoc;
    private boolean enableSwagger;

    public boolean isEnableRedoc() {
        return enableRedoc;
    }

    public void setEnableRedoc(boolean enableRedoc) {
        this.enableRedoc = enableRedoc;
    }

    public boolean isEnableSwagger() {
        return enableSwagger;
    }

    public void setEnableSwagger(boolean enableSwagger) {
        this.enableSwagger = enableSwagger;
    }
}
