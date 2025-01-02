package ject.componote.domain.faq.dao;

import ject.componote.domain.faq.domain.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
