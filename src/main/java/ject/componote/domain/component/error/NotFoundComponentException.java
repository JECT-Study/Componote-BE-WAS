package ject.componote.domain.component.error;

import org.springframework.http.HttpStatus;

public class NotFoundComponentException extends ComponentException {
    public NotFoundComponentException(final Long componentId) {
        super("컴포넌트를 찾을 수 없습니다. 컴포넌트 ID: " + componentId, HttpStatus.NOT_FOUND);
    }
}
