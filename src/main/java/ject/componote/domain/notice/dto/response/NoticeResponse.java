package ject.componote.domain.notice.dto.response;

import ject.componote.domain.notice.domain.Notice;

import java.time.LocalDate;

public record NoticeResponse(String title, String content, LocalDate createdDate) {
    public static NoticeResponse from(final Notice notice) {
        return new NoticeResponse(notice.getTitle().getValue(), notice.getContent().getValue(), notice.getCreatedAt().toLocalDate());
    }
}
