package ject.componote.domain.component.dto.find.response;

import ject.componote.domain.component.domain.Component;

public record ComponentSummaryResponse(
        Long id,
        String thumbnailUrl,
        String title,
        String introduction,
        String type,
        Long bookmarkCount,
        Long commentCount,
        Long designReferenceCount,
        Boolean isBookmarked) {
    public static ComponentSummaryResponse of(final Component component, final Boolean isBookmarked) {
        return new ComponentSummaryResponse(
                component.getId(),
                component.getSummary().getThumbnail().toUrl(),
                component.getSummary().getTitle(),
                component.getSummary().getIntroduction(),
                component.getType().name(),
                component.getBookmarkCount().getValue(),
                component.getCommentCount().getValue(),
                component.getDesignReferenceCount().getValue(),
                isBookmarked
        );
    }
}
