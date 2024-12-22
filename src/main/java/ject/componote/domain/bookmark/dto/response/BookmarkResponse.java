package ject.componote.domain.bookmark.dto.response;

import ject.componote.domain.bookmark.domain.Bookmark;

public record BookmarkResponse(Long bookmarkId) {

  public static BookmarkResponse from(Bookmark bookmark) {
    return new BookmarkResponse(
        bookmark.getId()
    );
  }
}

