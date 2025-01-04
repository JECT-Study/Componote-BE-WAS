package ject.componote.domain.design.application;

import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.domain.filter.DesignFilter;
import ject.componote.domain.design.dao.filter.DesignFilterRepository;
import ject.componote.domain.design.dao.DesignRepository;
import ject.componote.domain.design.domain.DesignSystem;
import ject.componote.domain.design.domain.link.DesignLink;
import ject.componote.domain.design.dao.link.DesignLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DesignSystemService {
    private final DesignRepository designRepository;
    private final DesignFilterRepository designFilterRepository;
    private final DesignLinkRepository designLinkRepository;

    private DesignSystem createDesignSystem(final Long designId) {
        final Design design = designRepository.findById(designId)
                .orElseThrow(IllegalArgumentException::new);
        final List<DesignFilter> filters = designFilterRepository.findAllByDesignId(designId);
        final List<DesignLink> links = designLinkRepository.findAllByDesignId(designId);
        return DesignSystem.of(design, links, filters);
    }
}
