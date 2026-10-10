package com.firearms.firearmcollectionjee.view.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Simple JSF converter for java.time.LocalDateTime.
 */
@FacesConverter(value = "localDateTimeConverter", managed = true)
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDateTime.parse(value, FORMATTER);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDateTime value) {
        if (value == null) return "";
        return value.format(FORMATTER);
    }
}
