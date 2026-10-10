package com.firearms.firearmcollectionjee.component;

import com.firearms.firearmcollectionjee.dto.firearm.function.FirearmToResponseFunction;
import com.firearms.firearmcollectionjee.dto.firearm.function.FirearmsToResponseFunction;
import com.firearms.firearmcollectionjee.dto.firearm.function.RequestToFirearmFunction;
import com.firearms.firearmcollectionjee.dto.firearm.function.RequestToFirearmWithNoWeaponFamilyFunction;
import com.firearms.firearmcollectionjee.dto.firearm.function.UpdateFirearmWithRequestFunction;
import com.firearms.firearmcollectionjee.dto.user.function.RequestToUserFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UpdateUserWithRequestFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UserToResponseFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UsersToResponseFunction;
import com.firearms.firearmcollectionjee.dto.weaponfamily.function.RequestToWeaponFamilyFunction;
import com.firearms.firearmcollectionjee.dto.weaponfamily.function.WeaponFamilyToResponseFunction;
import com.firearms.firearmcollectionjee.dto.weaponfamily.function.WeaponFamiliesToResponseFunction;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DtoFunctionFactory {

    public RequestToUserFunction requestToUserFunction() {
        return new RequestToUserFunction();
    }

    public UpdateUserWithRequestFunction updateUserWithRequestFunction() {
        return new UpdateUserWithRequestFunction();
    }

    public UserToResponseFunction userToResponseFunction() {
        return new UserToResponseFunction();
    }

    public UsersToResponseFunction usersToResponseFunction() {
        return new UsersToResponseFunction();
    }

    // Firearm functions
    public RequestToFirearmFunction requestToFirearmFunction() {
        return new RequestToFirearmFunction();
    }

    public RequestToFirearmWithNoWeaponFamilyFunction requestToFirearmWithNoWeaponFamilyFunction() {
        return new RequestToFirearmWithNoWeaponFamilyFunction();
    }

    public UpdateFirearmWithRequestFunction updateFirearmWithRequestFunction() {
        return new UpdateFirearmWithRequestFunction();
    }

    public FirearmToResponseFunction firearmToResponseFunction() {
        return new FirearmToResponseFunction();
    }

    public FirearmsToResponseFunction firearmsToResponseFunction() {
        return new FirearmsToResponseFunction();
    }

    // Weapon Family functions
    public RequestToWeaponFamilyFunction requestToWeaponFamilyFunction() {
        return new RequestToWeaponFamilyFunction();
    }

    public WeaponFamilyToResponseFunction weaponFamilyToResponseFunction() {
        return new WeaponFamilyToResponseFunction();
    }

    public WeaponFamiliesToResponseFunction weaponFamiliesToResponseFunction() {
        return new WeaponFamiliesToResponseFunction();
    }
}
