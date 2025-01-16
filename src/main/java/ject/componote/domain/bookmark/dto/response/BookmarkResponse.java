package ject.componote.domain.bookmark.dto.response;

import ject.componote.domain.bookmark.domain.Bookmark;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.design.domain.Design;

import java.time.LocalDateTime;

public record BookmarkResponse(
    Long bookmarkId,
    String type,
    Long resourceId,
    String resourceName,
    String organization,
    String thumbnailUrl,
    Long bookmarkCount,
    Long commentCount,
    LocalDateTime createdAt
) {
  // Component 북마크 변환
  public static BookmarkResponse from(Bookmark bookmark, Component component) {
    return new BookmarkResponse(
        bookmark.getId(),
        "component",
        component.getId(),
        component.getSummary().getTitle(),
        null,
        component.getSummary().getThumbnail().toUrl(),
        component.getBookmarkCount().getValue(),
        component.getCommentCount().getValue(),
        bookmark.getCreatedAt()
    );
  }

  // DesignSystem 북마크 변환
  public static BookmarkResponse from(Bookmark bookmark, Design designSystem) {
    return new BookmarkResponse(
        bookmark.getId(),
        "designSystem",
        designSystem.getId(),
        designSystem.getSummary().getName(),
        designSystem.getSummary().getOrganization(),
        designSystem.getSummary().getThumbnail().toUrl(),
        designSystem.getBookmarkCount().getValue(),
        null,
        bookmark.getCreatedAt()
    );
  }
}
