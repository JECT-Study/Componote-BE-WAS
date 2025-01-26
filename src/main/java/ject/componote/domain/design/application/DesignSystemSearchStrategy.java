package ject.componote.domain.design.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.design.dao.DesignRepository;
import ject.componote.domain.design.dao.DesignSystemRepository;
import ject.componote.domain.design.dao.filter.DesignFilterRepository;
import ject.componote.domain.design.dao.link.DesignLinkRepository;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.domain.filter.FilterType;
import ject.componote.domain.design.dto.search.request.DesignSystemSearchRequest;
import ject.componote.domain.design.error.NotFoundDesignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum DesignSystemSearchStrategy {
  WITH_BOOKMARK_AND_FILTER(
      (authPrincipal, request) -> isLoggedIn(authPrincipal) && hasFilter(request),
      (authPrincipal, designSystemRepository, filterRepository, linkRepository, request, pageable) ->
          findDesignsWithFilters(authPrincipal.id(), true, request, designSystemRepository, filterRepository, pageable)
  ),

  WITH_BOOKMARK(
      (authPrincipal, request) -> isLoggedIn(authPrincipal) && !hasFilter(request),
      (authPrincipal, designSystemRepository, filterRepository, linkRepository, request, pageable) ->
          designSystemRepository.findByKeywordAndBookmarkStatus(authPrincipal.id(), request.keyword(), pageable)
  ),

  WITHOUT_BOOKMARK_AND_FILTER(
      (authPrincipal, request) -> !isLoggedIn(authPrincipal) && hasFilter(request),
      (authPrincipal, designSystemRepository, filterRepository, linkRepository, request, pageable) ->
          findDesignsWithFilters(null, false, request, designSystemRepository, filterRepository, pageable)
  ),

  WITHOUT_BOOKMARK(
      (authPrincipal, request) -> !isLoggedIn(authPrincipal) && !hasFilter(request),
      (authPrincipal, designSystemRepository, filterRepository, linkRepository, request, pageable) ->
          designSystemRepository.findByKeyword(request.keyword(), pageable)
  );

  private final BiPredicate<AuthPrincipal, DesignSystemSearchRequest> condition;
  private final DesignSystemSearchFunction searchFunction;

  public static Page<Design> searchBy(final AuthPrincipal authPrincipal,
      final DesignSystemRepository designSystemRepository,
      final DesignFilterRepository filterRepository,
      final DesignLinkRepository linkRepository,
      final DesignSystemSearchRequest request,
      final Pageable pageable) {
    return Arrays.stream(values())
        .filter(strategy -> strategy.condition.test(authPrincipal, request))
        .findFirst()
        .orElseThrow(NotFoundDesignException::new)
        .searchFunction.search(authPrincipal, designSystemRepository, filterRepository, linkRepository, request, pageable);
  }

  private static boolean isLoggedIn(final AuthPrincipal authPrincipal) {
    return authPrincipal != null;
  }

  private static boolean hasFilter(final DesignSystemSearchRequest request) {
    return request.filters() != null && !request.filters().isEmpty();
  }

  private static Page<Design> findDesignsWithFilters(Long memberId, boolean includeBookmark,
      DesignSystemSearchRequest request,
      DesignSystemRepository designSystemRepository,
      DesignFilterRepository filterRepository,
      Pageable pageable) {
    List<Long> designIds = request.filters().stream()
        .flatMap(filter -> {
          FilterType filterType = FilterType.valueOf(filter.type().toUpperCase());
          return filterRepository.findAllDesignIdByCondition(filterType, filter.values()).stream();
        })
        .distinct()
        .collect(Collectors.toList());

    if (designIds.isEmpty()) {
      return Page.empty();
    }

    if (includeBookmark && memberId != null) {
      return designSystemRepository.findAllByIdInAndBookmarkStatus(memberId, designIds, pageable);
    }

    return designSystemRepository.findAllByIdIn(designIds, pageable);
  }

  @FunctionalInterface
  private interface DesignSystemSearchFunction {
    Page<Design> search(final AuthPrincipal authPrincipal,
        final DesignSystemRepository designSystemRepository,
        final DesignFilterRepository filterRepository,
        final DesignLinkRepository linkRepository,
        final DesignSystemSearchRequest request,
        final Pageable pageable);
  }
}
