package com.firearms.firearmcollectionjee.view.converter;

import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.service.WeaponFamilyService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

import java.util.UUID;

/**
 * JSF converter that resolves WeaponFamily entities by their UUID string id.
 * Registered as a managed converter so CDI injection works.
 */
@FacesConverter(value = "weaponFamilyConverter", managed = true)
public class WeaponFamilyConverter implements Converter<WeaponFamily> {

    @Inject
    private WeaponFamilyService weaponFamilyService;

    @Override
    public WeaponFamily getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;
        try {
            UUID id = UUID.fromString(value);
            return weaponFamilyService.findById(id).orElse(null);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, WeaponFamily value) {
        if (value == null || value.getId() == null) return "";
        return value.getId().toString();
    }
}
