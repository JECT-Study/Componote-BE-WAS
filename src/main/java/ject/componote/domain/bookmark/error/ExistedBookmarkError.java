package ject.componote.domain.bookmark.error;

import org.springframework.http.HttpStatus;

public class ExistedBookmarkError extends BookmarkException {
  public ExistedBookmarkError(final Long memberId, final Long resourceId, final String type) {
    super(String.format("해당 회원은 이미 %s를 북마크에 추가하였습니다. 소셜 ID : %d, %s ID : %d", type, memberId, type, resourceId), HttpStatus.BAD_REQUEST);
  }
}