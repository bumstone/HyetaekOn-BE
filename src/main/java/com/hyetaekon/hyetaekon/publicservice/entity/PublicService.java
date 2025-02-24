package com.hyetaekon.hyetaekon.publicservice.entity;

import com.hyetaekon.hyetaekon.publicservice.converter.BusinessTypeConverter;
import com.hyetaekon.hyetaekon.publicservice.converter.FamilyTypeConverter;
import com.hyetaekon.hyetaekon.publicservice.converter.OccupationConverter;
import com.hyetaekon.hyetaekon.publicservice.converter.SpecialGroupConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory serviceCategory;


    @Convert(converter = SpecialGroupConverter.class)  // 명시적 선언
    private SpecialGroup specialGroup;

    @Convert(converter = FamilyTypeConverter.class)
    private FamilyType familyType;

    @Convert(converter = OccupationConverter.class)
    private Occupation occupation;

    @Convert(converter = BusinessTypeConverter.class)
    private BusinessType businessType;

}
