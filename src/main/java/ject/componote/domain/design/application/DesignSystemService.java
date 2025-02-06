package ject.componote.domain.design.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public PageResponse<DesignSystemSearchResponse> searchDesignSystem(final AuthPrincipal authPrincipal,
                                                                       final DesignSystemSearchRequest request,
                                                                       final Pageable pageable) {
        List<Long> filteredDesignIds = getFilteredDesignIds(request);

        Page<Design> designs = searchByConditions(authPrincipal, request.keyword(), filteredDesignIds, pageable);

        List<Long> designIds = designs.getContent().stream().map(Design::getId).collect(Collectors.toList());

        Map<Long, List<DesignFilter>> filtersMap = designSystemRepository.findFiltersByDesignIds(designIds)
                .stream().collect(Collectors.groupingBy(DesignFilter::getDesignId));

        Map<Long, List<DesignLink>> linksMap = designSystemRepository.findLinksByDesignIds(designIds)
                .stream().collect(Collectors.groupingBy(DesignLink::getDesignId));

        Page<DesignSystemSearchResponse> responsePage = designs.map(design -> {
            List<DesignFilter> filters = filtersMap.getOrDefault(design.getId(), List.of());
            List<DesignLink> links = linksMap.getOrDefault(design.getId(), List.of());

            DesignSystem designSystem = DesignSystem.of(design, links, filters);
            return DesignSystemSearchResponse.from(designSystem);
        });

        return PageResponse.from(responsePage);
    }

    private Page<Design> searchByConditions(AuthPrincipal authPrincipal, String keyword, List<Long> filteredDesignIds, Pageable pageable) {
        if (authPrincipal != null) {
            return !filteredDesignIds.isEmpty()
                    ? designSystemRepository.findAllByIdInAndBookmarkStatus(authPrincipal.id(), filteredDesignIds, pageable)
                    : designSystemRepository.findByKeywordAndBookmarkStatus(authPrincipal.id(), keyword, pageable);
        }
        return !filteredDesignIds.isEmpty()
                ? designSystemRepository.findAllByIdIn(filteredDesignIds, pageable)
                : designSystemRepository.findByKeyword(keyword, pageable);
    }

    private List<Long> getFilteredDesignIds(DesignSystemSearchRequest request) {
        if (request.filters() == null || request.filters().isEmpty()) {
            return List.of();
        }

        return request.filters().stream()
                .flatMap(filter -> {
                    List<Long> designIds = designSystemRepository.findAllDesignIdByCondition(
                            filter.parseType(),
                            filter.values() != null ? filter.values() : List.of()
                    );
                    return designIds.stream();
                })
                .distinct()
                .collect(Collectors.toList());
    }

}
