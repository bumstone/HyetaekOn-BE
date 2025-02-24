package com.hyetaekon.hyetaekon.publicservice.converter;


import com.hyetaekon.hyetaekon.common.converter.GenericCodeConverter;
import com.hyetaekon.hyetaekon.publicservice.entity.BusinessType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BusinessTypeConverter extends GenericCodeConverter<BusinessType> {
    public BusinessTypeConverter() {
        super(BusinessType.class);
    }
}
