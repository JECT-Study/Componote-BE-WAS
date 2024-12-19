package ject.componote.domain.bookmark.error;

import org.springframework.http.HttpStatus;

public class NotFoundBookmarkException extends BookmarkException {
  public NotFoundBookmarkException(final Long memberId, final Long componentId) {
    super(String.format("해당 회원의 일치하는 북마크를 찾을 수 없습니다. 회원 ID : %d, 컴포넌트 ID : %d", memberId, componentId), HttpStatus.NOT_FOUND);
  }
}
