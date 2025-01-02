package ject.componote.domain.component.dao;

import ject.componote.domain.component.domain.ComponentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComponentQueryDsl {
    Page<ComponentSummaryDao> searchByKeywordWithTypes(final String keyword, final List<ComponentType> types, final Pageable pageable);
    Page<ComponentSummaryDao> searchByKeyword(final String keyword, final Pageable pageable);
    Page<ComponentSummaryDao> searchWithBookmark(final Long memberId, final String keyword, final Pageable pageable);
    Page<ComponentSummaryDao> searchWithBookmarkAndTypes(final Long memberId, final String keyword, final List<ComponentType> types, final Pageable pageable);
}
