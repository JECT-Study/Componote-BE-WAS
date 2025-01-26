package ject.componote.domain.design.application;

import java.util.List;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.design.dao.DesignSystemRepository;
import ject.componote.domain.design.dao.filter.DesignFilterRepository;
import ject.componote.domain.design.dao.link.DesignLinkRepository;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.domain.DesignSystem;
import ject.componote.domain.design.domain.filter.DesignFilter;
import ject.componote.domain.design.domain.link.DesignLink;
import ject.componote.domain.design.dto.search.request.DesignSystemSearchRequest;
import ject.componote.domain.design.dto.search.response.DesignSystemSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DesignSystemService {

    private final DesignSystemRepository designSystemRepository;
    private final DesignFilterRepository designFilterRepository;
    private final DesignLinkRepository designLinkRepository;

    public PageResponse<DesignSystemSearchResponse> searchDesignSystem(final AuthPrincipal authPrincipal,
                                                                       final DesignSystemSearchRequest request,
                                                                       final Pageable pageable) {

        Page<Design> designs = DesignSystemSearchStrategy.searchBy(
                authPrincipal,
                designSystemRepository,
                designFilterRepository,
                designLinkRepository,
                request,
                pageable
        );

        Page<DesignSystemSearchResponse> responsePage = designs.map(design -> {
            List<DesignFilter> filters = designFilterRepository.findAllByDesignId(design.getId());
            List<DesignLink> links = designLinkRepository.findAllByDesignId(design.getId());
            DesignSystem designSystem = DesignSystem.of(design, links, filters);
            return DesignSystemSearchResponse.from(designSystem);
        });

        return PageResponse.from(responsePage);
    }
}

