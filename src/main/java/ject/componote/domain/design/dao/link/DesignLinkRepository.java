package ject.componote.domain.design.dao.link;

import ject.componote.domain.design.domain.filter.DesignFilter;
import ject.componote.domain.design.domain.link.DesignLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesignLinkRepository extends JpaRepository<DesignLink, Long> {
    List<DesignLink> findAllByDesignId(final Long designId);
}
