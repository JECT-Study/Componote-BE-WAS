package ject.componote.infra.oauth.error.profile;

import ject.componote.infra.oauth.error.OAuthClientException;
import ject.componote.infra.oauth.error.profile.response.OAuthProfileErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuthProfileException extends OAuthClientException {
    public OAuthProfileException(final OAuthProfileErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        log.error("소셜 프로필 조회 실패. 에러 코드 : {}, 에러 메시지 : {}", errorResponse.getErrorCode(), errorResponse.getMessage());
    }
}
