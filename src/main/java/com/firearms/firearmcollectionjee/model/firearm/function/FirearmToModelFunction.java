package com.firearms.firearmcollectionjee.model.firearm.function;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.model.firearm.FirearmModel;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class FirearmToModelFunction implements Function<Firearm, FirearmModel> {

    @Override
    public FirearmModel apply(Firearm f) {
        if (f == null) return null;
        return FirearmModel.builder()
                .id(f.getId())
                .name(f.getName())
                .caliber(f.getCaliber())
                .magazineCapacity(f.getMagazineCapacity())
                .productionDate(f.getProductionDate())
                .build();
    }
}
