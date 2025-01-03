package ject.componote.domain.bookmark.error;

import org.springframework.http.HttpStatus;

public class NotFoundComponentException extends BookmarkException {
  public NotFoundComponentException(final Long componentId) {
    super(String.format("일치하는 컴포넌트를 찾을 수 없습니다. 컴포넌트 ID : %d", componentId), HttpStatus.NOT_FOUND);
  }
}
