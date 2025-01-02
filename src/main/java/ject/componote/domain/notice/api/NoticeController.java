package ject.componote.domain.notice.api;

import ject.componote.domain.notice.application.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;


}
