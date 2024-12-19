package ject.componote.domain.bookmark.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByUserIdAndComponentId(Long userId, Long componentId);

    List<Bookmark> findAllByUserId(Long userId);

    Optional<Bookmark> findByUserIdAndComponentId(Long userId, Long componentId);

    Page<Bookmark> findAllByUserId(Long userId, Pageable pageable);
}
