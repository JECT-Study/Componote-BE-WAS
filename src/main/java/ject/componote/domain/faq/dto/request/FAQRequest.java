package ject.componote.domain.faq.dto.request;

import jakarta.validation.constraints.NotNull;
import ject.componote.domain.faq.api.FAQTypeConstant;

public record FAQRequest(@NotNull FAQTypeConstant type) {
}
