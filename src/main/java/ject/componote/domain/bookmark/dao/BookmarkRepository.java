package ject.componote.domain.bookmark.dao;

import ject.componote.domain.bookmark.domain.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkQueryDsl {
    boolean existsByMemberIdAndResourceIdAndType(Long memberId, Long resourceId, String type);
    Optional<Bookmark> findByMemberIdAndResourceIdAndType(Long memberId, Long resourceId, String type);
    Page<Bookmark> findAllByMemberIdAndType(Long memberId, String type, Pageable pageable);
}
