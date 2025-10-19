package com.firearms.firearmcollectionjee.dto.firearm.function;

import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmsResponse;
import com.firearms.firearmcollectionjee.model.Firearm;

import java.util.List;
import java.util.function.Function;

public class FirearmsToResponseFunction implements Function<List<Firearm>, GetFirearmsResponse> {
    @Override
    public GetFirearmsResponse apply(List<Firearm> entities) {
        return GetFirearmsResponse.builder()
                .firearms(entities.stream()
                        .map(firearm -> GetFirearmsResponse.Firearm.builder()
                                .id(firearm.getId())
                                .name(firearm.getName())
                                .build())
                        .toList())
                .build();
    }

}
