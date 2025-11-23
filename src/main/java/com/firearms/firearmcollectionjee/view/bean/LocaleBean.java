package com.firearms.firearmcollectionjee.view.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Locale;

@Named
@SessionScoped
public class LocaleBean implements Serializable {

    private Locale locale;

    public Locale getLocale() {
        if (locale == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (context.getViewRoot() != null) {
                locale = context.getViewRoot().getLocale();
            } else {
                locale = context.getApplication().getDefaultLocale();
            }
        }
        return locale;
    }

    public String getLanguage() {
        return getLocale().getLanguage();
    }

    public void changeLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
}
