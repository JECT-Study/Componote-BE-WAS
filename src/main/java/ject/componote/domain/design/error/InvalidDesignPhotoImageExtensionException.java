package ject.componote.domain.design.error;

import org.springframework.http.HttpStatus;

public class InvalidDesignPhotoImageExtensionException extends DesignException {
    public InvalidDesignPhotoImageExtensionException(final String extension) {
        super("확장자가 올바르지 않습니다. 입력된 확장자: " + extension, HttpStatus.BAD_REQUEST);
    }
}
