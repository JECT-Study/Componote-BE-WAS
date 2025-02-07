package ject.componote.domain.design.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.design.dao.DesignSystemRepository;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.domain.DesignSystem;
import ject.componote.domain.design.domain.filter.DesignFilter;
import ject.componote.domain.design.domain.link.DesignLink;
import ject.componote.domain.design.dto.search.request.DesignSystemSearchRequest;
import ject.componote.domain.design.dto.search.response.DesignSystemSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j // 로그 추가
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DesignSystemService {

    private final DesignSystemRepository designSystemRepository;

    public PageResponse<DesignSystemSearchResponse> searchDesignSystem(final AuthPrincipal authPrincipal,
                                                                       final DesignSystemSearchRequest request,
                                                                       final Pageable pageable) {
        log.info("🔎 [START] searchDesignSystem() 호출 - AuthPrincipal: {}, Request: {}, Pageable: {}", authPrincipal, request, pageable);

        // 1️⃣ 필터 조건에 맞는 디자인 ID 조회
        List<Long> filteredDesignIds = getFilteredDesignIds(request);
        log.info("📝 [FILTER] 필터링된 Design ID 목록: {}", filteredDesignIds);

        // 2️⃣ 조건에 맞는 디자인 목록 조회
        Page<Design> designs = searchByConditions(authPrincipal, request.keyword(), filteredDesignIds, pageable);
        log.info("📌 [DESIGNS] 조회된 Design 개수: {}, 내용: {}", designs.getTotalElements(), designs.getContent());

        List<Long> designIds = designs.getContent().stream().map(Design::getId).collect(Collectors.toList());

        // 3️⃣ DesignFilter 조회
        Map<Long, List<DesignFilter>> filtersMap = designSystemRepository.findFiltersByDesignIds(designIds)
                .stream().collect(Collectors.groupingBy(DesignFilter::getDesignId));
        log.info("🔗 [FILTER MAP] DesignFilter 개수: {}, 데이터: {}", filtersMap.size(), filtersMap);

        // 4️⃣ DesignLink 조회
        Map<Long, List<DesignLink>> linksMap = designSystemRepository.findLinksByDesignIds(designIds)
                .stream().collect(Collectors.groupingBy(DesignLink::getDesignId));
        log.info("🌍 [LINK MAP] DesignLink 개수: {}, 데이터: {}", linksMap.size(), linksMap);

        // 5️⃣ 최종 변환
        Page<DesignSystemSearchResponse> responsePage = designs.map(design -> {
            List<DesignFilter> filters = filtersMap.getOrDefault(design.getId(), List.of());
            List<DesignLink> links = linksMap.getOrDefault(design.getId(), List.of());

            DesignSystem designSystem = DesignSystem.of(design, links, filters);
            return DesignSystemSearchResponse.from(designSystem);
        });

        log.info("✅ [RESULT] 최종 반환되는 데이터 개수: {}, 내용: {}", responsePage.getTotalElements(), responsePage.getContent());

        return PageResponse.from(responsePage);
    }

    private Page<Design> searchByConditions(AuthPrincipal authPrincipal, String keyword, List<Long> filteredDesignIds, Pageable pageable) {
        log.info("🔍 [SEARCH CONDITIONS] AuthPrincipal: {}, keyword: {}, filteredDesignIds: {}, pageable: {}",
                authPrincipal, keyword, filteredDesignIds, pageable);

        Page<Design> result;

        if (authPrincipal != null) {
            if (!filteredDesignIds.isEmpty()) {
                result = designSystemRepository.findAllByIdInAndBookmarkStatus(authPrincipal.id(), filteredDesignIds, pageable);
            } else if (keyword != null && !keyword.isBlank()) {
                result = designSystemRepository.findByKeywordAndBookmarkStatus(authPrincipal.id(), keyword, pageable);
            } else {
                result = designSystemRepository.findAllByBookmarkStatus(authPrincipal.id(), pageable); // 🔥 수정된 부분: keyword가 없으면 전체 조회
            }
        } else {
            if (!filteredDesignIds.isEmpty()) {
                result = designSystemRepository.findAllByIdIn(filteredDesignIds, pageable);
            } else if (keyword != null && !keyword.isBlank()) {
                result = designSystemRepository.findByKeyword(keyword, pageable);
            } else {
                result = designSystemRepository.findAll(pageable); // 🔥 수정된 부분: keyword가 없으면 전체 조회
            }
        }

        log.info("📄 [SEARCH RESULT] 조회된 Design 개수: {}, 내용: {}", result.getTotalElements(), result.getContent());
        return result;
    }

    private List<Long> getFilteredDesignIds(DesignSystemSearchRequest request) {
        if (request.filters() == null || request.filters().isEmpty()) {
            log.info("🚫 [FILTER] 필터가 없으므로 모든 디자인 포함");
            return List.of();
        }

        List<Long> result = request.filters().stream()
                .flatMap(filter -> {
                    List<Long> designIds = designSystemRepository.findAllDesignIdByCondition(
                            filter.parseType(),
                            filter.values() != null ? filter.values() : List.of()
                    );
                    log.info("📌 [FILTER] {} 필터로 검색된 Design ID: {}", filter.parseType(), designIds);
                    return designIds.stream();
                })
                .distinct()
                .collect(Collectors.toList());

        log.info("✅ [FINAL FILTERED IDs] 최종 필터링된 Design ID 목록: {}", result);
        return result;
    }
}
