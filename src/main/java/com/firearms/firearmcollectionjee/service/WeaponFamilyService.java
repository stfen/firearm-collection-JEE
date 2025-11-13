package com.firearms.firearmcollectionjee.service;

import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.repository.api.WeaponFamilyRepositoryInterface;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import com.firearms.firearmcollectionjee.service.FirearmService;
import jakarta.transaction.Transactional;
import jakarta.annotation.security.RolesAllowed;
import com.firearms.firearmcollectionjee.entity.enums.UserRoles;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(force = true)
@LocalBean
@Stateless
public class WeaponFamilyService {
    private final WeaponFamilyRepositoryInterface weaponFamilyRepository;
    private final FirearmService firearmService;

    @Inject
    public WeaponFamilyService(WeaponFamilyRepositoryInterface weaponFamilyRepository, FirearmService firearmService){
        this.weaponFamilyRepository = weaponFamilyRepository;
        this.firearmService = firearmService;
    }

    @RolesAllowed(UserRoles.ADMIN)
    public void createWeaponFamily(WeaponFamily weaponFamily) {
        if (weaponFamily == null) {
            throw new IllegalArgumentException("WeaponFamily cannot be null");
        }
        weaponFamilyRepository.create(weaponFamily);
    }

    @RolesAllowed(UserRoles.ADMIN)
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

    @RolesAllowed(UserRoles.ADMIN)
    public void deleteWeaponFamily(UUID id) {
        weaponFamilyRepository.delete(weaponFamilyRepository.findById(id).orElseThrow());
    }

    @RolesAllowed(UserRoles.USER)
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
