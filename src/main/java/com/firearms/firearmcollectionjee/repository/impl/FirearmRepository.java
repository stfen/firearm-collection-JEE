package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.model.Firearm;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.model.WeaponFamily;
import com.firearms.firearmcollectionjee.repository.api.FirearmRepositoryInterface;
import com.firearms.firearmcollectionjee.storage.DataStorage;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestScoped
@NoArgsConstructor(force = true)
public class FirearmRepository implements FirearmRepositoryInterface {

    private final DataStorage dataStorage;

    @Inject
    public FirearmRepository(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public void create(Firearm firearm) {
        dataStorage.createFirearm(firearm);
    }

    @Override
    public void update(Firearm firearm) {
        dataStorage.updateFirearm(firearm);
    }

    @Override
    public void delete(Firearm firearm) {
        dataStorage.deleteFirearm(firearm);
    }


    @Override
    public Optional<Firearm> findById(UUID id) {
        return dataStorage.findAllFirearms().stream()
                .filter(firearm -> firearm.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Firearm> findAllByUser(User user) {
        return dataStorage.findAllFirearms().stream()
                .filter(firearm -> user.equals(firearm.getUser()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Firearm> findAllByWeaponFamily(WeaponFamily weaponFamily) {
        return dataStorage.findAllFirearms().stream()
                .filter(firearm -> weaponFamily.equals(firearm.getWeaponFamily()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Firearm> findAll(){
        return dataStorage.findAllFirearms();
    }
}
