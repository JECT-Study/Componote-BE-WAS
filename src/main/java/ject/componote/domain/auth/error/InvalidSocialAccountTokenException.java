package ject.componote.domain.auth.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class InvalidSocialAccountTokenException extends AuthException {
    public InvalidSocialAccountTokenException(final String socialAccountToken) {
        super("유효하지 않은 socialAccountToken 입니다.", HttpStatus.BAD_REQUEST);
        log.error("socialAccountToken: {}", socialAccountToken);
    }
}
