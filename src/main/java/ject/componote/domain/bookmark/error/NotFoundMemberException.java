package ject.componote.domain.bookmark.error;

import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends BookmarkException {
  public NotFoundMemberException(final Long memberId) {
    super(String.format("일치하는 회원을 찾을 수 없습니다. 회원 ID : %d", memberId), HttpStatus.NOT_FOUND);
  }
}
