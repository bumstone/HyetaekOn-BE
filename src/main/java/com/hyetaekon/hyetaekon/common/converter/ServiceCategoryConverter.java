package com.hyetaekon.hyetaekon.common.converter;


import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.publicservice.entity.ServiceCategory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // autoApply를 true로 설정하면 @Convert없이 해당 타입에 대해 자동으로 변환
public class ServiceCategoryConverter implements AttributeConverter<ServiceCategory, String> {

    @Override
    public String convertToDatabaseColumn(ServiceCategory serviceCategory) {
        return serviceCategory.getType();
    }

    @Override
    public ServiceCategory convertToEntityAttribute(String dbData) {
        for (ServiceCategory serviceCategory : ServiceCategory.values()) {
            if (serviceCategory.getType().equals(dbData)) {
                return serviceCategory;
            }
        }
        throw new GlobalException(ErrorCode.SERVICE_CATEGORY_NOT_FOUND);
    }

}
