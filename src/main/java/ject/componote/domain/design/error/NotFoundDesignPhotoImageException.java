package ject.componote.domain.design.error;

import org.springframework.http.HttpStatus;

public class NotFoundDesignPhotoImageException extends DesignException {
    public NotFoundDesignPhotoImageException() {
        super("디자인의 사진이 없습니다.", HttpStatus.NOT_FOUND);
    }
}
