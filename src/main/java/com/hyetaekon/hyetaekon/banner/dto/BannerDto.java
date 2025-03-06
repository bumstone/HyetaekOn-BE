package com.hyetaekon.hyetaekon.banner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannerDto {
    private Long id;
    private String title;
    private String imageUrl;
    private int order;
}
