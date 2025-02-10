package ject.componote.domain.bookmark.dao;

import java.util.Set;

public interface ComponentBookmarkQueryDsl {
    Set<Long> findAllComponentIdsByMemberIdAndComponentIds(final Long memberId, final Set<Long> componentIds);
}
