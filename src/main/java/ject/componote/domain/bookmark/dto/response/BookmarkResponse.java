package ject.componote.domain.bookmark.dto.response;

import ject.componote.domain.bookmark.domain.Bookmark;
import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.design.domain.DesignSystem;

import java.time.LocalDateTime;

public record BookmarkResponse(
    Long bookmarkId,
    String type,
    Long resourceId,
    String resourceName,
    String organization,
    BaseImage thumbnailUrl,
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
        component.getSummary().getThumbnail(),
        component.getBookmarkCount().getValue(),
        component.getCommentCount().getValue(),
        bookmark.getCreatedAt()
    );
  }

  // DesignSystem 북마크 변환
  public static BookmarkResponse from(Bookmark bookmark, DesignSystem designSystem) {
    return new BookmarkResponse(
        bookmark.getId(),
        "designSystem",
        designSystem.getDesign().getId(),
        designSystem.getDesign().getSummary().getName(),
        designSystem.getDesign().getSummary().getOrganization(),
        designSystem.getDesign().getSummary().getThumbnail(),
        designSystem.getDesign().getBookmarkCount().getValue(),
        null,
        bookmark.getCreatedAt()
    );
  }
}
