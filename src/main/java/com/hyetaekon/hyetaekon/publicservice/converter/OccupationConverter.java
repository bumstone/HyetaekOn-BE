package com.hyetaekon.hyetaekon.publicservice.converter;

import com.hyetaekon.hyetaekon.publicservice.entity.Occupation;
import com.hyetaekon.hyetaekon.common.converter.GenericCodeConverter;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OccupationConverter extends GenericCodeConverter<Occupation> {
    public OccupationConverter() {
        super(Occupation.class);
    }
}
