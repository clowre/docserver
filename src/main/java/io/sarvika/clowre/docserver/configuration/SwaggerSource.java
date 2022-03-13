package io.sarvika.clowre.docserver.configuration;

import javax.validation.constraints.NotEmpty;

public class SwaggerSource {

    @NotEmpty
    private String name;

    @NotEmpty
    private String pathPrefix;

    @NotEmpty
    private String address;

    @NotEmpty
    private SchemaVersion version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SchemaVersion getVersion() {
        return version;
    }

    public void setVersion(SchemaVersion version) {
        this.version = version;
    }
}
