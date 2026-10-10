package com.firearms.firearmcollectionjee.view.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Locale;

@Named
@SessionScoped
public class LocaleBean implements Serializable {

    private String language = "en"; // Default to English

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        // Update the view locale
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getViewRoot() != null) {
            context.getViewRoot().setLocale(new Locale(language));
        }
    }

    public Locale getLocale() {
        return new Locale(language);
    }
}
