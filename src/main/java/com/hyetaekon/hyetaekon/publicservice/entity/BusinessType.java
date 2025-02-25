package com.hyetaekon.hyetaekon.publicservice.entity;

import com.hyetaekon.hyetaekon.publicservice.converter.BusinessTypeConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "business_type")
public class BusinessType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "public_service_id", nullable = false)
    private PublicService publicService;

    // TODO: 사업체 형태 - 회원 정보
    @Convert(converter = BusinessTypeConverter.class)
    private BusinessTypeEnum businessTypeEnum;
}