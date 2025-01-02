package ject.componote.domain.notice.application;

import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.notice.dao.NoticeRepository;
import ject.componote.domain.notice.dto.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public PageResponse<NoticeResponse> getNotices(final Pageable pageable) {
        final Page<NoticeResponse> page = noticeRepository.findAllWithPagination(pageable)
                .map(NoticeResponse::from);
        return PageResponse.from(page);
    }
}
