package com.firearms.firearmcollectionjee.component;

import com.firearms.firearmcollectionjee.model.firearm.function.FirearmToModelFunction;
import com.firearms.firearmcollectionjee.model.weaponfamily.WeaponFamiliesModel;
import com.firearms.firearmcollectionjee.model.weaponfamily.function.WeaponFamiliesToModelFunction;
import com.firearms.firearmcollectionjee.model.weaponfamily.function.WeaponFamilyToModelFunction;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Factory for model conversion functions (entity -> view model).
 * Provides centralized creation of model mapping functions for injection.
 */
@ApplicationScoped
public class ModelFunctionFactory {

    public WeaponFamilyToModelFunction weaponFamilyToModel() {
        return new WeaponFamilyToModelFunction();
    }

    public WeaponFamiliesToModelFunction weaponFamiliesToModel() {return new WeaponFamiliesToModelFunction(); }

    public FirearmToModelFunction firearmToModel() {
        return new FirearmToModelFunction();
    }
}
