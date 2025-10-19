package com.firearms.firearmcollectionjee.repository.api;

import com.firearms.firearmcollectionjee.model.Firearm;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.model.WeaponFamily;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FirearmRepositoryInterface {
    void create(Firearm firearm);
    void update(Firearm firearm);
    void delete(Firearm firearm);
    Optional<Firearm> findById(UUID id);
    List<Firearm> findAllByUser(User user);
    List<Firearm> findAllByWeaponFamily(WeaponFamily weaponFamily);
}
