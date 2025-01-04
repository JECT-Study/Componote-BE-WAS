package ject.componote.domain.design.dao;

import ject.componote.domain.design.domain.Design;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignRepository extends JpaRepository<Design, Long> {
}
