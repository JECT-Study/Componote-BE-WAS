package ject.componote.domain.bookmark.error;

import org.springframework.http.HttpStatus;

public class InvalidBookmarkTypeError extends BookmarkException {
  public InvalidBookmarkTypeError(final String type) {
    super(String.format("유효하지 않은 북마크 타입입니다: %s", type), HttpStatus.BAD_REQUEST);
  }
}