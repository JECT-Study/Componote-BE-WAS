package ject.componote.domain.component.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.dao.ComponentSummaryDao;
import ject.componote.domain.component.dto.find.request.ComponentSearchRequest;
import ject.componote.domain.component.error.InvalidCommentSearchStrategyException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.function.BiPredicate;

@RequiredArgsConstructor
public enum ComponentSearchStrategy {
    WITH_BOOKMARK_AND_FILTER(
            (authPrincipal, request) -> isLoggedIn(authPrincipal) && hasFilter(request),
            (authPrincipal, componentRepository, request, pageable) ->
                    componentRepository.searchWithBookmarkAndTypes(authPrincipal.id(), request.keyword(), request.types(), pageable)
    ),

    WITH_BOOKMARK(
            (authPrincipal, request) -> isLoggedIn(authPrincipal) && !hasFilter(request),
            (authPrincipal, componentRepository, request, pageable) ->
                    componentRepository.searchWithBookmark(authPrincipal.id(), request.keyword(), pageable)
    ),

    WITHOUT_BOOKMARK_AND_FILTER(
            (authPrincipal, request) -> !isLoggedIn(authPrincipal) && hasFilter(request),
            (authPrincipal, componentRepository, request, pageable) ->
                    componentRepository.searchByKeywordWithTypes(request.keyword(), request.types(), pageable)
    ),

    WITHOUT_BOOKMARK(
            (authPrincipal, request) -> !isLoggedIn(authPrincipal) && !hasFilter(request),
            (authPrincipal, componentRepository, request, pageable) ->
                    componentRepository.searchByKeyword(request.keyword(), pageable)
    );

    private final BiPredicate<AuthPrincipal, ComponentSearchRequest> condition;
    private final ComponentSearchFunction searchFunction;

    public static Page<ComponentSummaryDao> searchBy(final AuthPrincipal authPrincipal,
                                                     final ComponentRepository componentRepository,
                                                     final ComponentSearchRequest request,
                                                     final Pageable pageable) {
        return Arrays.stream(values())
                .filter(strategy -> strategy.condition.test(authPrincipal, request))
                .findFirst()
                .orElseThrow(InvalidCommentSearchStrategyException::new)
                .searchFunction.search(authPrincipal, componentRepository, request, pageable);
    }

    private static boolean isLoggedIn(final AuthPrincipal authPrincipal) {
        return authPrincipal != null;
    }

    private static boolean hasFilter(final ComponentSearchRequest request) {
        return request.types() != null && !request.types().isEmpty();
    }

    @FunctionalInterface
    private interface ComponentSearchFunction {
        Page<ComponentSummaryDao> search(final AuthPrincipal authPrincipal, final ComponentRepository componentRepository, final ComponentSearchRequest request, final Pageable pageable);
    }
}
