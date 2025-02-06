package ject.componote.domain.bookmark.dto.response;

import ject.componote.domain.bookmark.domain.ComponentBookmark;
import ject.componote.domain.component.domain.Component;

import java.time.LocalDateTime;

public record ComponentBookmarkResponse(
        Long bookmarkId,
        Long componentId,
        String componentName,
        String thumbnailUrl,
        Long bookmarkCount,
        Long commentCount,
        LocalDateTime createdAt
) {
    public static ComponentBookmarkResponse of(ComponentBookmark bookmark, Component component) {
        return new ComponentBookmarkResponse(
                bookmark.getId(),
                component.getId(),
                component.getSummary().getTitle(),
                component.getSummary().getThumbnail().toUrl(),
                component.getBookmarkCount().getValue(),
                component.getCommentCount().getValue(),
                bookmark.getCreatedAt()
        );
    }
}