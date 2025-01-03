package ject.componote.domain.faq.dto.response;

import ject.componote.domain.faq.domain.FAQ;

public record FAQResponse(String title, String content) {
    public static FAQResponse from(final FAQ faq) {
        return new FAQResponse(faq.getTitle().getValue(), faq.getContent().getValue());
    }
}
