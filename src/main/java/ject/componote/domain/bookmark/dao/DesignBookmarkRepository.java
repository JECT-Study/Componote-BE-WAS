package ject.componote.domain.bookmark.dao;

import ject.componote.domain.bookmark.domain.DesignBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignBookmarkRepository extends JpaRepository<DesignBookmark, Long> {
    Page<DesignBookmark> findAllByMemberId(Long memberId, Pageable pageable);
    boolean existsByMemberIdAndDesignId(Long memberId, Long designId);
    Optional<DesignBookmark> findByMemberIdAndDesignId(Long memberId, Long designId);
}
