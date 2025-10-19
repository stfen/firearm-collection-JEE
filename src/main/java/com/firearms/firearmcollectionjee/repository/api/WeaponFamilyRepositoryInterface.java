package com.firearms.firearmcollectionjee.repository.api;

import com.firearms.firearmcollectionjee.model.Firearm;
import com.firearms.firearmcollectionjee.model.WeaponFamily;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WeaponFamilyRepositoryInterface {
    void create(WeaponFamily weaponFamily);
    void update(WeaponFamily weaponFamily);
    void delete(WeaponFamily weaponFamily);
    Optional<WeaponFamily> findById(UUID id);
    List<WeaponFamily> findAll();
}
