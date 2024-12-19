package ject.componote.domain.bookmark.error;

import org.springframework.http.HttpStatus;

public class ExistedComponentError extends BookmarkException {
  public ExistedComponentError(final Long memberId, final Long componentId) {
    super(String.format("해당 회원은 이미 해당 컴포넌트를 북마크에 추가하였습니다. 소셜 ID : %d, 컴포넌트 ID : %d", memberId, componentId), HttpStatus.BAD_REQUEST);
  }
}