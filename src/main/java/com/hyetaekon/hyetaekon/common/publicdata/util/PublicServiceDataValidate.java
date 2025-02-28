package com.hyetaekon.hyetaekon.common.publicdata.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

import static com.hyetaekon.hyetaekon.common.exception.ErrorCode.*;

@Slf4j
@Component
public class PublicServiceDataValidate {

    /**
     * 공공 서비스 데이터 검증 및 예외 처리
     */
    public <T> T validateAndHandleException(PublicServiceDataOperation<T> operation, PublicDataPath apiPath) {
        try {
            return operation.execute();
        } catch (InvalidFormatException e) {
            log.error("❌ 데이터 조회 실패: 파라미터 또는 서비스 키를 확인해주세요. [API_PATH: {}]", apiPath.getPath(), e);
            throw new GlobalException(INVALID_PUBLIC_SERVICE_DATA);
        } catch (URISyntaxException e) {
            log.error("❌ 데이터 조회 실패: URI 구문 오류. [API_PATH: {}]", apiPath.getPath(), e);
            throw new GlobalException(PUBLIC_SERVICE_URI_ERROR);
        } catch (Exception e) {
            log.error("❌ 공공서비스 데이터 조회 중 예기치 않은 오류 발생. [API_PATH: {}]", apiPath.getPath(), e);
            throw new GlobalException(PUBLIC_SERVICE_API_ERROR);
        }
    }

    /**
     * 데이터 조회 및 처리 작업을 위한 함수형 인터페이스
     */
    @FunctionalInterface
    public interface PublicServiceDataOperation<T> {
        T execute() throws Exception;
    }
}
