package ject.componote.domain.notice.api;

import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.notice.application.NoticeService;
import ject.componote.domain.notice.dto.response.NoticeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeService noticeService;

    public ResponseEntity<PageResponse<NoticeResponse>> getNotices(@PageableDefault final Pageable pageable) {
        return ResponseEntity.ok(
                noticeService.getNotices(pageable)
        );
    }
}
