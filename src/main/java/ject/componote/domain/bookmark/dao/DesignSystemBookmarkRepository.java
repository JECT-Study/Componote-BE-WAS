package ject.componote.domain.bookmark.dao;

import ject.componote.domain.bookmark.domain.DesignSystemBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignSystemBookmarkRepository extends JpaRepository<DesignSystemBookmark, Long> {
    Page<DesignSystemBookmark> findAllByMemberId(Long memberId, Pageable pageable);
    boolean existsByMemberIdAndDesignId(Long memberId, Long designId);
    Optional<DesignSystemBookmark> findByMemberIdAndDesignId(Long memberId, Long designId);
}
