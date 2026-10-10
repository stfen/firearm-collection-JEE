package com.firearms.firearmcollectionjee.repository.api;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.User;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FirearmRepositoryInterface {
    void create(Firearm firearm);
    void update(Firearm firearm);
    void delete(Firearm firearm);
    List<Firearm> findAll();
    Optional<Firearm> findById(UUID id);
    List<Firearm> findAllByUser(User user);
    List<Firearm> findAllByWeaponFamily(WeaponFamily weaponFamily);
    Optional<Firearm> findByIdAndUser(UUID id, User user);
}
