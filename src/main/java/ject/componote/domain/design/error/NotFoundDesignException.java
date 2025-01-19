package ject.componote.domain.design.error;

import org.springframework.http.HttpStatus;

public class NotFoundDesignException extends DesignException {
    public NotFoundDesignException() {
        super("디자인이 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
