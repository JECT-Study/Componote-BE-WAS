package ject.componote.domain.faq.api;

import jakarta.validation.Valid;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.faq.application.FAQService;
import ject.componote.domain.faq.dto.request.FAQRequest;
import ject.componote.domain.faq.dto.response.FAQResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/faqs")
@RestController
@RequiredArgsConstructor
public class FAQController {
    private final FAQService faqService;

    @GetMapping
    public ResponseEntity<PageResponse<FAQResponse>> getFAQs(@ModelAttribute @Valid final FAQRequest faqRequest,
                                                             @PageableDefault final Pageable pageable) {
        return ResponseEntity.ok(
                faqService.getFAQs(faqRequest, pageable)
        );
    }
}
