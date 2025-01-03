package ject.componote.domain.component.error;

import org.springframework.http.HttpStatus;

public class NotFoundComponentThumbnailException extends ComponentException {
    public NotFoundComponentThumbnailException() {
        super("이미지 objectKey를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
