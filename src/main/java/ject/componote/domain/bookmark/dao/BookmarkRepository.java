package ject.componote.domain.bookmark.dao;

import ject.componote.domain.bookmark.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByComponentIdAndMemberId(final Long componentId, final Long memberId);
}
