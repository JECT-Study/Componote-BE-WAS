package ject.componote.domain.faq.application;

import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.faq.api.FAQTypeConstant;
import ject.componote.domain.faq.dao.FAQRepository;
import ject.componote.domain.faq.domain.FAQ;
import ject.componote.domain.faq.domain.FAQType;
import ject.componote.domain.faq.dto.request.FAQRequest;
import ject.componote.domain.faq.dto.response.FAQResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FAQService {
    private final FAQRepository faqRepository;

    public PageResponse<FAQResponse> getFAQs(final FAQRequest request, final Pageable pageable) {
        final FAQTypeConstant typeConstant = request.type();
        final Page<FAQResponse> page = findAllFAQsWithConditions(typeConstant, pageable)
                .map(FAQResponse::from);
        return PageResponse.from(page);
    }

    private Page<FAQ> findAllFAQsWithConditions(final FAQTypeConstant typeConstant, final Pageable pageable) {
        if (typeConstant.isAll()) {
            return faqRepository.findAll(pageable);
        }

        final FAQType type = typeConstant.getType();
        return faqRepository.findAllByType(type, pageable);
    }
}
