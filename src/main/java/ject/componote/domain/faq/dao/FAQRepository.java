package ject.componote.domain.faq.dao;

import ject.componote.domain.faq.domain.FAQ;
import ject.componote.domain.faq.domain.FAQType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    Page<FAQ> findAllByType(final FAQType type, final Pageable pageable);
}
