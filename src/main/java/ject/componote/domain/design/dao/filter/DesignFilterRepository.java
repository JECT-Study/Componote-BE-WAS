package ject.componote.domain.design.dao.filter;

import ject.componote.domain.design.domain.filter.DesignFilter;
import ject.componote.domain.design.domain.filter.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesignFilterRepository extends JpaRepository<DesignFilter, Long> {
    List<DesignFilter> findAllByDesignId(@Param("designId") final Long designId);

    @Query("SELECT DISTINCT df.designId FROM DesignFilter df " +
            "INNER JOIN Design d ON d.id = df.designId " +
            "WHERE df.type =:type AND df.value IN :values")
    List<Long> findAllDesignIdByCondition(@Param("type") final FilterType type, @Param("values") final List<String> values);
}
