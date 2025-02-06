package ject.componote.domain.design.dao;

import java.util.List;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.domain.filter.DesignFilter;
import ject.componote.domain.design.domain.filter.FilterType;
import ject.componote.domain.design.domain.link.DesignLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DesignSystemRepository extends JpaRepository<Design, Long> {

  @Query(value = "SELECT DISTINCT d FROM Design d WHERE d.id IN :designIds",
          countQuery = "SELECT COUNT(d) FROM Design d WHERE d.id IN :designIds")
  Page<Design> findAllByIdIn(@Param("designIds") List<Long> designIds, Pageable pageable);

  @Query(value = "SELECT DISTINCT d FROM Design d WHERE d.summary.name LIKE %:keyword%",
          countQuery = "SELECT COUNT(d) FROM Design d WHERE d.summary.name LIKE %:keyword%")
  Page<Design> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

  @Query(value = "SELECT DISTINCT d FROM Design d WHERE d.id IN :designIds " +
          "AND EXISTS (SELECT 1 FROM Bookmark b WHERE b.memberId = :userId AND b.resourceId = d.id)",
          countQuery = "SELECT COUNT(d) FROM Design d WHERE d.id IN :designIds " +
                  "AND EXISTS (SELECT 1 FROM Bookmark b WHERE b.memberId = :userId AND b.resourceId = d.id)")
  Page<Design> findAllByIdInAndBookmarkStatus(@Param("userId") Long userId, @Param("designIds") List<Long> designIds, Pageable pageable);

  @Query(value = "SELECT DISTINCT d FROM Design d WHERE d.summary.name LIKE %:keyword% " +
          "AND EXISTS (SELECT 1 FROM Bookmark b WHERE b.memberId = :userId AND b.resourceId = d.id)",
          countQuery = "SELECT COUNT(d) FROM Design d WHERE d.summary.name LIKE %:keyword% " +
                  "AND EXISTS (SELECT 1 FROM Bookmark b WHERE b.memberId = :userId AND b.resourceId = d.id)")
  Page<Design> findByKeywordAndBookmarkStatus(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);


  @Query("SELECT df FROM DesignFilter df WHERE df.designId IN :designIds")
  List<DesignFilter> findFiltersByDesignIds(@Param("designIds") List<Long> designIds);

  @Query("SELECT dl FROM DesignLink dl WHERE dl.designId IN :designIds")
  List<DesignLink> findLinksByDesignIds(@Param("designIds") List<Long> designIds);

  @Query("SELECT DISTINCT df.designId FROM DesignFilter df " +
          "WHERE df.type = :type AND df.value IN :values")
  List<Long> findAllDesignIdByCondition(@Param("type") FilterType type, @Param("values") List<String> values);

}
