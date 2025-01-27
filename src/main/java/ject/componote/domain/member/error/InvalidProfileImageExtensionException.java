package ject.componote.domain.member.error;

import org.springframework.http.HttpStatus;

public class InvalidProfileImageExtensionException extends MemberException {
    public InvalidProfileImageExtensionException(final String extension) {
        super("확장자가 올바르지 않습니다. 입력된 확장자: " + extension, HttpStatus.BAD_REQUEST);
    }
}
