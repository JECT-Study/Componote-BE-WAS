package ject.componote.domain.bookmark.dao;

import ject.componote.domain.bookmark.domain.ComponentBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentBookmarkRepository extends JpaRepository<ComponentBookmark, Long> {
    Page<ComponentBookmark> findAllByMemberId(Long memberId, Pageable pageable);
    boolean existsByMemberIdAndComponentId(Long memberId, Long componentId);
    Optional<ComponentBookmark> findByMemberIdAndComponentId(Long memberId, Long componentId);
}
