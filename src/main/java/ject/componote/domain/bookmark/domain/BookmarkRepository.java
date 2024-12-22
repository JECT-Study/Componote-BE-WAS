package ject.componote.domain.bookmark.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByMemberIdAndComponentId(Long memberId, Long componentId);

    Optional<Bookmark> findByMemberIdAndComponentId(Long memberId, Long componentId);

    Page<Bookmark> findAllByMemberId(Long memberId, Pageable pageable);
}
