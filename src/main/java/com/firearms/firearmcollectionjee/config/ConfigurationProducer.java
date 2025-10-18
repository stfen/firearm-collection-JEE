package com.firearms.firearmcollectionjee.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.inject.Inject;

/**
 * CDI producer for configuration values that need to be injected.
 */
@ApplicationScoped
public class ConfigurationProducer {

    @Inject
    private ServletContext servletContext;

    @Produces
    @Named("avatarBasePath")
    public String produceAvatarBasePath() {
        return servletContext.getInitParameter("avatars.path");
    }
}