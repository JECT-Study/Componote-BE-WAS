package ject.componote.domain.announcement.dao;

import ject.componote.domain.announcement.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
