package com.hyetaekon.hyetaekon.publicservice.entity;

import com.hyetaekon.hyetaekon.publicservice.converter.FamilyTypeConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "family_type")
public class FamilyType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "public_service_id", nullable = false)
    private PublicService publicService;

    // TODO: 가구 형태 - 검색 + 해시태그
    @Convert(converter = FamilyTypeConverter.class)
    private FamilyTypeEnum familyTypeEnum;
}