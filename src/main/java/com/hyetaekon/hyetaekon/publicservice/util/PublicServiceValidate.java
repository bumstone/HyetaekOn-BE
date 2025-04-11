package com.hyetaekon.hyetaekon.publicservice.util;

import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.publicservice.entity.ServiceCategory;
import com.hyetaekon.hyetaekon.publicservice.repository.PublicServiceRepository;
import com.hyetaekon.hyetaekon.user.entity.User;
import com.hyetaekon.hyetaekon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PublicServiceValidate {
    public final PublicServiceRepository publicServiceRepository;
    public final UserRepository userRepository;

    public PublicService validateServiceById(Long serviceId) {
        return publicServiceRepository.findById(serviceId)
            .orElseThrow(() -> new GlobalException(ErrorCode.SERVICE_NOT_FOUND_BY_ID));
    }

    public User validateUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_ID));
    }

    public ServiceCategory validateServiceCategory(String categoryName) {
        try {
            return ServiceCategory.valueOf(categoryName);
        } catch (IllegalArgumentException e) {
            throw new GlobalException(ErrorCode.SERVICE_CATEGORY_NOT_FOUND);
        }
    }
}
