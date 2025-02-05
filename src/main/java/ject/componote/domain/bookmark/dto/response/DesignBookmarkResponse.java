package ject.componote.domain.bookmark.dto.response;

import ject.componote.domain.bookmark.domain.DesignBookmark;
import ject.componote.domain.design.domain.Design;

import java.time.LocalDateTime;

public record DesignBookmarkResponse(
        Long bookmarkId,
        Long designId,
        String designName,
        String organization,
        String thumbnailUrl,
        Long bookmarkCount,
        LocalDateTime createdAt
) {
    public static DesignBookmarkResponse of(DesignBookmark bookmark, Design design) {
        return new DesignBookmarkResponse(
                bookmark.getId(),
                design.getId(),
                design.getSummary().getName(),
                design.getSummary().getOrganization(),
                design.getSummary().getThumbnail().toUrl(),
                design.getBookmarkCount().getValue(),
                bookmark.getCreatedAt()
        );
    }
}