package com.firearms.firearmcollectionjee.controller.api;

import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmResponse;
import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmsResponse;
import com.firearms.firearmcollectionjee.dto.firearm.PatchFirearmRequest;
import com.firearms.firearmcollectionjee.dto.firearm.PutFirearmRequest;

import java.util.UUID;

public interface FirearmControllerInterface {
    GetFirearmsResponse getFirearms();
    GetFirearmsResponse getWeaponFamilyFirearms(UUID id);
    GetFirearmsResponse getUserFirearms(UUID id);
    GetFirearmResponse getFirearm(UUID id);
    void putFirearm(UUID id, PutFirearmRequest firearm);
    void patchFirearm(UUID id, PatchFirearmRequest firearm);
    void deleteFirearm(UUID id);
}
