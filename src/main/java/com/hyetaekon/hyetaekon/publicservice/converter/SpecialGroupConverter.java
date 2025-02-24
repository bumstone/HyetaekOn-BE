package com.hyetaekon.hyetaekon.publicservice.converter;

import com.hyetaekon.hyetaekon.publicservice.entity.SpecialGroup;
import com.hyetaekon.hyetaekon.common.converter.GenericCodeConverter;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SpecialGroupConverter extends GenericCodeConverter<SpecialGroup> {
    public SpecialGroupConverter() {
        super(SpecialGroup.class);
    }
}