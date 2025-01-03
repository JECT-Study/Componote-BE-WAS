package ject.componote.domain.component.dao;

import ject.componote.domain.component.domain.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComponentRepository extends JpaRepository<Component, Long>, ComponentQueryDsl {
    @Query(
            nativeQuery = true,
            value = "UPDATE component c SET c.view_count = c.view_count + 1  WHERE c.id =:id"
    )
    @Modifying(clearAutomatically = true)
    void increaseViewCount(@Param("id") final Long id); // 변경 감지 대신 사용할 메서드
}
