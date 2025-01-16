package ject.componote.domain.bookmark.dao;

public interface BookmarkQueryDsl {
    boolean existsByComponentIdAndMemberId(final Long componentId, final Long memberId);
}
