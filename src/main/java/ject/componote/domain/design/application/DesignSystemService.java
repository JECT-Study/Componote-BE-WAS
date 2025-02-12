package ject.componote.domain.design.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dao.DesignBookmarkRepository;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.design.dao.DesignSystemRepository;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.domain.DesignSystem;
import ject.componote.domain.design.domain.filter.DesignFilter;
import ject.componote.domain.design.domain.filter.FilterType;
import ject.componote.domain.design.domain.link.DesignLink;
import ject.componote.domain.design.dto.search.request.DesignSystemSearchRequest;
import ject.componote.domain.design.dto.search.request.DesignSystemSummaryRequest;
import ject.componote.domain.design.dto.search.response.DesignSystemSearchResponse;
import ject.componote.domain.design.dto.search.response.DesignSystemSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DesignSystemService {

    private final DesignSystemRepository designSystemRepository;
    private final DesignBookmarkRepository designBookmarkRepository;

    public PageResponse<DesignSystemSummaryResponse> getAllDesignSummaries(final AuthPrincipal authPrincipal,
                                                                           final DesignSystemSummaryRequest request,
                                                                           final Pageable pageable) {
        // ✅ 필터 파싱 (List<String> → Map<FilterType, List<String>>)
        Map<FilterType, List<String>> parsedFilters = parseFilters(request.filters());
        log.info("📝 [FILTER] 파싱된 필터: {}", parsedFilters);

        // ✅ 필터 조건에 맞는 디자인 ID 조회
        List<Long> filteredDesignIds = getFilteredDesignIds(parsedFilters);
        log.info("📌 [FILTERED IDs] 최종 필터링된 Design ID 목록: {}", filteredDesignIds);

        // ✅ 조건에 맞는 디자인 목록 조회
        Page<Design> designs = searchByConditions(authPrincipal, filteredDesignIds, pageable);
        log.info("📌 [DESIGNS] 조회된 Design 개수: {}, 내용: {}", designs.getTotalElements(), designs.getContent());

        List<Long> designIds = designs.getContent().stream().map(Design::getId).collect(Collectors.toList());

        // ✅ DesignFilter 조회
        Map<Long, List<DesignFilter>> filtersMap = designSystemRepository.findFiltersByDesignIds(designIds)
                .stream().collect(Collectors.groupingBy(DesignFilter::getDesignId));
        log.info("🔗 [FILTER MAP] DesignFilter 개수: {}, 데이터: {}", filtersMap.size(), filtersMap);

        // ✅ DesignLink 조회
        Map<Long, List<DesignLink>> linksMap = designSystemRepository.findLinksByDesignIds(designIds)
                .stream().collect(Collectors.groupingBy(DesignLink::getDesignId));
        log.info("🌍 [LINK MAP] DesignLink 개수: {}, 데이터: {}", linksMap.size(), linksMap);

        // ✅ 최종 변환
        Page<DesignSystemSummaryResponse> responsePage = designs.map(design -> {
            List<DesignFilter> filters = filtersMap.getOrDefault(design.getId(), List.of());
            List<DesignLink> links = linksMap.getOrDefault(design.getId(), List.of());

            DesignSystem designSystem = DesignSystem.of(design, links, filters);
            boolean isBookmarked = isBookmarked(authPrincipal, design.getId());
            return DesignSystemSummaryResponse.from(designSystem, isBookmarked);
        });

        log.info("✅ [RESULT] 최종 반환되는 데이터 개수: {}, 내용: {}", responsePage.getTotalElements(), responsePage.getContent());

        return PageResponse.from(responsePage);
    }

    public PageResponse<DesignSystemSearchResponse> searchDesigns(final DesignSystemSearchRequest request,
                                                                  final Pageable pageable) {
        Page<Design> designs = designSystemRepository.searchByKeyword(request.keyword(), pageable);
        log.info("📌 [SEARCH RESULTS] 조회된 Design 개수: {}, 내용: {}", designs.getTotalElements(), designs.getContent());

        List<Long> designIds = designs.getContent().stream().map(Design::getId).collect(Collectors.toList());

        Map<Long, List<DesignLink>> linksMap = designSystemRepository.findLinksByDesignIds(designIds)
                .stream().collect(Collectors.groupingBy(DesignLink::getDesignId));

        Page<DesignSystemSearchResponse> responsePage = designs.map(design -> {
            List<DesignLink> links = linksMap.getOrDefault(design.getId(), List.of());

            DesignSystem designSystem = DesignSystem.of(design, links, List.of());
            return DesignSystemSearchResponse.from(designSystem);
        });

        log.info("✅ [RESULT] 최종 반환되는 데이터 개수: {}, 내용: {}", responsePage.getTotalElements(), responsePage.getContent());

        return PageResponse.from(responsePage);
    }



    private Map<FilterType, List<String>> parseFilters(List<String> filterValues) {
        if (filterValues == null || filterValues.isEmpty()) {
            return Collections.emptyMap();
        }

        return Arrays.stream(FilterType.values())
                .collect(Collectors.toMap(
                        type -> type,
                        type -> filterValues.stream()
                                .filter(type::contains)
                                .collect(Collectors.toList()),
                        (existing, replacement) -> existing, // 중복 키 방지
                        HashMap::new
                ));
    }

    /**
     * 🔥 필터 조건에 맞는 Design ID 목록 조회
     */
    private List<Long> getFilteredDesignIds(Map<FilterType, List<String>> filters) {
        if (filters.isEmpty()) {
            return List.of();
        }

        return filters.entrySet().stream()
                .flatMap(entry -> {
                    FilterType type = entry.getKey();
                    List<String> values = entry.getValue();

                    List<Long> designIds = designSystemRepository.findAllDesignIdByCondition(type, values);
                    log.info("📌 [FILTER] {} 필터로 검색된 Design ID: {}", type, designIds);
                    return designIds.stream();
                })
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 🔍 조건에 따라 `Design`을 조회하는 메서드
     */
    private Page<Design> searchByConditions(AuthPrincipal authPrincipal, List<Long> filteredDesignIds, Pageable pageable) {
        log.info("🔍 [SEARCH CONDITIONS] AuthPrincipal: {}, filteredDesignIds: {}, pageable: {}",
                authPrincipal, filteredDesignIds, pageable);

        if (authPrincipal != null) {
            if (!filteredDesignIds.isEmpty()) {
                return designSystemRepository.findAllByIdInAndBookmarkStatus(authPrincipal.id(), filteredDesignIds, pageable);
            } else {
                return designSystemRepository.findAllByBookmarkStatus(authPrincipal.id(), pageable);
            }
        } else {
            if (!filteredDesignIds.isEmpty()) {
                return designSystemRepository.findAllByIdIn(filteredDesignIds, pageable);
            } else {
                return designSystemRepository.findAll(pageable);
            }
        }
    }

    private boolean isBookmarked(final AuthPrincipal authPrincipal, final Long designId) {
        if (!isLoggedIn(authPrincipal)) {
            return false;
        }
        return designBookmarkRepository.existsByMemberIdAndDesignId(authPrincipal.id(), designId);
    }

    private boolean isLoggedIn(final AuthPrincipal authPrincipal) {
        return authPrincipal != null;
    }
}
