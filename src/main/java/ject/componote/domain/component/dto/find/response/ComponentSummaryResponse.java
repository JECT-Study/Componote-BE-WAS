package ject.componote.domain.component.dto.find.response;

import ject.componote.domain.component.dao.ComponentSummaryDao;
import ject.componote.domain.component.domain.summary.ComponentSummary;

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
    public static ComponentSummaryResponse from(final ComponentSummaryDao dao) {
        final ComponentSummary summary = dao.summary();
        return new ComponentSummaryResponse(
                dao.id(),
                summary.getThumbnail().toUrl(),
                summary.getTitle(),
                summary.getIntroduction(),
                dao.type().name(),
                dao.bookmarkCount().getValue(),
                dao.commentCount().getValue(),
                dao.designReferenceCount().getValue(),
                dao.isBookmarked()
        );
    }
}
