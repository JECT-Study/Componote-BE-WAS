package ject.componote.domain.bookmark.error;

import org.springframework.http.HttpStatus;

public class NotFoundDesignSystemException extends BookmarkException {
  public NotFoundDesignSystemException(final Long designSystemId) {
    super(String.format("일치하는 디자인시스템을 찾을 수 없습니다. 디자인시스템 ID : %d", designSystemId), HttpStatus.NOT_FOUND);
  }
}
