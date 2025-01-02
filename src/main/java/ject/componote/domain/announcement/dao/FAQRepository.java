package ject.componote.domain.announcement.dao;

import ject.componote.domain.announcement.domain.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
