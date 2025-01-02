package ject.componote.domain.component.dao;

import ject.componote.domain.common.model.Count;
import ject.componote.domain.component.domain.ComponentType;
import ject.componote.domain.component.domain.summary.ComponentSummary;

public record ComponentSummaryDao(
        Long id,
        ComponentSummary summary,
        ComponentType type,
        Count bookmarkCount,
        Count commentCount,
        Count designReferenceCount,
        Count viewCount,
        Boolean isBookmarked
) {
}
