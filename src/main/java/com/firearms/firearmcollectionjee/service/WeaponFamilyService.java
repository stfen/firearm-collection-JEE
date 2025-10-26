package com.firearms.firearmcollectionjee.service;

import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.repository.api.WeaponFamilyRepositoryInterface;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.firearms.firearmcollectionjee.service.FirearmService;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class WeaponFamilyService {
    private final WeaponFamilyRepositoryInterface weaponFamilyRepository;
    private final FirearmService firearmService;

    @Inject
    public WeaponFamilyService(WeaponFamilyRepositoryInterface weaponFamilyRepository, FirearmService firearmService){
        this.weaponFamilyRepository = weaponFamilyRepository;
        this.firearmService = firearmService;
    }

    public void createWeaponFamily(WeaponFamily weaponFamily) {
        if (weaponFamily == null) {
            throw new IllegalArgumentException("WeaponFamily cannot be null");
        }
        weaponFamilyRepository.create(weaponFamily);
    }

    public void updateWeaponFamily(WeaponFamily weaponFamily) {
        if (weaponFamily == null) {
            throw new IllegalArgumentException("WeaponFamily cannot be null");
        }
        weaponFamilyRepository.update(weaponFamily);
    }

    public Optional<WeaponFamily> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("WeaponFamily ID cannot be null");
        }
        return weaponFamilyRepository.findById(id);
    }

    public void deleteWeaponFamily(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("WeaponFamily ID cannot be null");
        }
        // delete all firearms that belong to this weapon family first
        firearmService.findAllByWeaponFamily(id).ifPresent(list -> list.forEach(f -> firearmService.deleteFirearm(f.getId())));
        // then delete the family itself
        weaponFamilyRepository.findById(id).ifPresent(weaponFamilyRepository::delete);
    }

    public List<WeaponFamily> getAllWeaponFamilies() {
        return weaponFamilyRepository.findAll();
    }

    public List<WeaponFamily> findAll() {
        return weaponFamilyRepository.findAll();
    }

    public boolean weaponFamilyExists(UUID id) {
        if (id == null) {
            return false;
        }
        return weaponFamilyRepository.findById(id).isPresent();
    }
}
