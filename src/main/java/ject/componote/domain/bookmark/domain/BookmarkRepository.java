package ject.componote.domain.bookmark.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByMemberIdAndResourceIdAndType(Long memberId, Long resourceId, String type);

    Optional<Bookmark> findByMemberIdAndResourceIdAndType(Long memberId, Long resourceId, String type);

    Page<Bookmark> findAllByMemberIdAndType(Long memberId, String type, Pageable pageable);
}
