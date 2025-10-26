package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.repository.api.WeaponFamilyRepositoryInterface;
import com.firearms.firearmcollectionjee.storage.DataStorage;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
public class WeaponFamilyRepository implements WeaponFamilyRepositoryInterface {

    private final DataStorage dataStorage;

    @Inject
    public WeaponFamilyRepository(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public void create(WeaponFamily weaponFamily) {
        dataStorage.createWeaponFamily(weaponFamily);
    }

    @Override
    public void update(WeaponFamily weaponFamily) {
        dataStorage.updateWeaponFamily(weaponFamily);
    }

    @Override
    public void delete(WeaponFamily weaponFamily) {
        dataStorage.deleteWeaponFamily(weaponFamily);
    }

    @Override
    public Optional<WeaponFamily> findById(UUID id) {
        return dataStorage.findAllWeaponFamilies().stream()
                .filter(weaponFamily -> weaponFamily.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<WeaponFamily> findAll(){
        return dataStorage.findAllWeaponFamilies();
    }
}
