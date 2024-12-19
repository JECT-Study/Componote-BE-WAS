package ject.componote.domain.bookmark.error;

import ject.componote.global.error.ComponoteException;
import org.springframework.http.HttpStatus;

public class BookmarkException  extends ComponoteException {
  public BookmarkException(final String message, final HttpStatus status) {
    super(message, status);
  }
}
