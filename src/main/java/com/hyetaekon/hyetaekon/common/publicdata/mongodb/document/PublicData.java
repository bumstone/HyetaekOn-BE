package com.hyetaekon.hyetaekon.common.publicdata.mongodb.document;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "service_info")
public class PublicData {
    @Id
    private String id;

    private String publicServiceId;
    private String serviceName;
    private String summaryPurpose;
    private String serviceCategory;
    private List<String> specialGroup;
    private List<String> familyType;
}
