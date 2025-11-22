package com.firearms.firearmcollectionjee.view.weaponfamily;

import com.firearms.firearmcollectionjee.model.weaponfamily.WeaponFamiliesModel;
import com.firearms.firearmcollectionjee.service.WeaponFamilyService;
import com.firearms.firearmcollectionjee.component.ModelFunctionFactory;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.UUID;

/**
 * JSF backing bean for the weapon family list view.
 * Renamed from WeaponFamilyListBean to WeaponFamilyList to match the project's naming pattern.
 */
@Named("weaponFamilyList")
@ViewScoped
public class WeaponFamilyList implements Serializable {

    private final WeaponFamilyService weaponFamilyService;
    private WeaponFamiliesModel weaponFamilies;
    private final ModelFunctionFactory modelFactory;

    @Inject
    public WeaponFamilyList(WeaponFamilyService weaponFamilyService, ModelFunctionFactory modelFactory) {
        this.weaponFamilyService = weaponFamilyService;
        this.modelFactory = modelFactory;
    }

    public WeaponFamiliesModel getWeaponFamilies() {
        if (weaponFamilies == null) {
            weaponFamilies = modelFactory.weaponFamiliesToModel().apply(weaponFamilyService.findAll());
        }
        return weaponFamilies;
    }

    public String delete(UUID id) {
        if (id == null) return null;
        weaponFamilyService.deleteWeaponFamily(id);
        return "/weaponfamily/weaponfamily_list.xhtml?faces-redirect=true";
    }

}
