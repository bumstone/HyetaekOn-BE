package com.hyetaekon.hyetaekon.publicservice.converter;

import com.hyetaekon.hyetaekon.publicservice.entity.FamilyType;
import com.hyetaekon.hyetaekon.common.converter.GenericCodeConverter;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FamilyTypeConverter extends GenericCodeConverter<FamilyType> {
    public FamilyTypeConverter() {
        super(FamilyType.class);
    }
}
