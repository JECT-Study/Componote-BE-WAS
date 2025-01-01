package ject.componote.domain.bookmark.error;

import org.springframework.http.HttpStatus;

public class NotFoundBookmarkException extends BookmarkException {
  public NotFoundBookmarkException(final Long memberId, final Long resourceId, final String type) {
    super(String.format("해당 회원의 일치하는 북마크를 찾을 수 없습니다. 회원 ID : %d, %s ID : %d", memberId, type, resourceId), HttpStatus.NOT_FOUND);
  }
}
