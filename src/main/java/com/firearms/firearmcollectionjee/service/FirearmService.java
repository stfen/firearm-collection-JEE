package com.firearms.firearmcollectionjee.service;

import com.firearms.firearmcollectionjee.model.Firearm;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.model.WeaponFamily;
import com.firearms.firearmcollectionjee.repository.api.FirearmRepositoryInterface;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;
import com.firearms.firearmcollectionjee.repository.impl.FirearmRepository;
import com.firearms.firearmcollectionjee.repository.impl.UserRepository;
import com.firearms.firearmcollectionjee.repository.impl.WeaponFamilyRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@NoArgsConstructor(force = true)
public class FirearmService {

    private final FirearmRepository firearmRepository;
    private final UserRepository userRepository;
    private final WeaponFamilyRepository weaponFamilyRepository;

    @Inject
    public FirearmService(FirearmRepository firearmRepository, UserRepository userRepository, WeaponFamilyRepository weaponFamilyRepository) {
        this.firearmRepository = firearmRepository;
        this.userRepository = userRepository;
        this.weaponFamilyRepository = weaponFamilyRepository;
    }

    public void createFirearm(Firearm firearm) {
       firearmRepository.create(firearm);
    }

    public void updateFirearm(Firearm firearm) {
        firearmRepository.update(firearm);
    }

    public void deleteFirearm(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Firearm ID cannot be null");
        }
        firearmRepository.findById(id).ifPresent(firearmRepository::delete);
    }

    public Optional<Firearm> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return firearmRepository.findById(id);
    }

    public List<Firearm> findAll() {
        return firearmRepository.findAll();
    }

    public Optional<List<Firearm>> findAllByUser(UUID id) {
        return userRepository.findById(id)
                .map(firearmRepository::findAllByUser);
    }

    public Optional<List<Firearm>> findAllByWeaponFamily(UUID id) {
        return weaponFamilyRepository.findById(id)
                .map(firearmRepository::findAllByWeaponFamily);
    }


}
