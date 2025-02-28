package com.hyetaekon.hyetaekon.common.publicdata.service;

import com.hyetaekon.hyetaekon.common.publicdata.util.PublicDataPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static com.hyetaekon.hyetaekon.common.publicdata.util.PublicDataConstants.SUBSIDY_DATA_END_POINT;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicServiceDataProviderService {
    @Value("${PUBLIC_SERVICE_INFO_KEY}")
    private String serviceKey;

    /**
     * 공공데이터 URI 생성 메서드
     */
    public URI createUri(PublicDataPath publicDataPath, String... params) throws URISyntaxException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(
                SUBSIDY_DATA_END_POINT + publicDataPath.getPath())
            .queryParam("serviceKey", serviceKey)
            .queryParam("type", "json");

        for (int i = 0; i < params.length; i += 2) {
            if (i + 1 < params.length) {
                uriBuilder.queryParam(params[i], params[i + 1]);
            }
        }

        String uriString = uriBuilder.build().toUriString();
        log.debug("생성된 URI: {}", uriString);

        return new URI(uriString);
    }

    /**
     * 서비스 ID를 포함한 Path Variable URI 생성 메서드
     */
    public URI createUriWithPathVariable(PublicDataPath publicDataPath, String serviceId) throws URISyntaxException {
        String path = publicDataPath.getPath().replace("{serviceId}", serviceId);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(
                SUBSIDY_DATA_END_POINT + path)
            .queryParam("serviceKey", serviceKey)
            .queryParam("type", "json");

        String uriString = uriBuilder.build().toUriString();
        log.debug("생성된 URI: {}", uriString);

        return new URI(uriString);
    }
}
