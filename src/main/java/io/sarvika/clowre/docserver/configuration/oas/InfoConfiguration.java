package io.sarvika.clowre.docserver.configuration.oas;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@ConfigurationProperties(value = "swagger.info")
public class InfoConfiguration extends Info {

    private InfoContactConfiguration contact;
    private LicenseConfiguration license;

    @Override
    public InfoContactConfiguration getContact() {
        return contact;
    }

    public void setContact(InfoContactConfiguration contact) {
        this.contact = contact;
    }

    @Override
    public License getLicense() {
        return super.getLicense();
    }

    public void setLicense(LicenseConfiguration license) {
        this.license = license;
    }

    @Introspected
    @ConfigurationProperties("contact")
    public static class InfoContactConfiguration extends Contact {
    }

    @Introspected
    @ConfigurationProperties("license")
    public static class LicenseConfiguration extends License {
    }
}
