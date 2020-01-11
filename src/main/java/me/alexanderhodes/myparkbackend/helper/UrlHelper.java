package me.alexanderhodes.myparkbackend.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class UrlHelper {

    @Value("${mypark.frontend.url}")
    private String frontendUrl;

    public UrlHelper() {}

    public String getPasswordResetUrl(String token) {
        return new StringBuffer(this.frontendUrl).append("common/password/").append(token).toString();
    }

    public String getConfirmRegistrationUrl(String token) {
        return new StringBuffer(this.frontendUrl).append("common/confirm-registration/").append(token).toString();
    }

    public String getFrontendUrl() {
        return this.frontendUrl;
    }

}
