package ject.componote.domain.design.dao;

import ject.componote.domain.design.domain.Design;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Long> {
    @Query("SELECT d FROM Design d WHERE d.id IN :designIds AND d.summary.name LIKE %:keyword%")
    Page<Design> findAllByIdInAndKeyword(@Param("designIds") List<Long> designIds, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT d FROM Design d WHERE d.summary.name LIKE %:keyword%")
    Page<Design> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT d FROM Design d WHERE d.id IN :designIds")
    Page<Design> findAllByIdIn(@Param("designIds") List<Long> designIds, Pageable pageable);

    @Query("SELECT d FROM Design d WHERE d.id IN :designIds AND EXISTS (" +
            "SELECT b FROM Bookmark b WHERE b.memberId = :userId AND b.resourceId = d.id)")
    Page<Design> findAllByIdInAndBookmarkStatus(@Param("userId") Long userId, @Param("designIds") List<Long> designIds, Pageable pageable);

    @Query("SELECT d FROM Design d WHERE d.summary.name LIKE %:keyword% AND EXISTS (" +
            "SELECT b FROM Bookmark b WHERE b.memberId = :userId AND b.resourceId = d.id)")
    Page<Design> findByKeywordAndBookmarkStatus(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

}
