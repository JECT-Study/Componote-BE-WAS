package ject.componote.domain.component.dao;

import ject.componote.domain.component.domain.Component;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<Component, Long>, ComponentQueryDsl {
}
