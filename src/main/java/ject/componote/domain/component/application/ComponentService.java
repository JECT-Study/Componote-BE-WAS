package ject.componote.domain.component.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dao.BookmarkRepository;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.dao.ComponentSummaryDao;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentType;
import ject.componote.domain.component.dto.event.ComponentViewCountIncreaseEvent;
import ject.componote.domain.component.dto.find.request.ComponentSearchRequest;
import ject.componote.domain.component.dto.find.response.ComponentDetailResponse;
import ject.componote.domain.component.dto.find.response.ComponentSummaryResponse;
import ject.componote.domain.component.error.NotFoundComponentException;
import ject.componote.domain.component.util.ComponentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComponentService {
    private final ApplicationEventPublisher eventPublisher;
    private final BookmarkRepository bookmarkRepository;
    private final ComponentMapper componentMapper;
    private final ComponentRepository componentRepository;

    public ComponentDetailResponse getComponentDetail(final AuthPrincipal authPrincipal, final Long componentId) {
        final Component component = findComponentById(componentId);
        eventPublisher.publishEvent(ComponentViewCountIncreaseEvent.from(component));
        return componentMapper.mapFrom(component, isBookmarked(authPrincipal, componentId));
    }

    public PageResponse<ComponentSummaryResponse> search(final AuthPrincipal authPrincipal,
                                                         final ComponentSearchRequest request,
                                                         final Pageable pageable) {
        final Page<ComponentSummaryResponse> page = searchByConditions(authPrincipal, request, pageable)
                .map(ComponentSummaryResponse::from);
        return PageResponse.from(page);
    }

    private Component findComponentById(final Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundComponentException(componentId));
    }

    private Page<ComponentSummaryDao> searchByConditions(final AuthPrincipal authPrincipal,
                                                         final ComponentSearchRequest request,
                                                         final Pageable pageable) {
        final String keyword = request.keyword();
        final List<ComponentType> types = request.types();
        if (isLoggedIn(authPrincipal)) {
            return componentRepository.findAllByKeywordAndTypesWithBookmark(authPrincipal.id(), keyword, types, pageable);
        }

        return componentRepository.findAllByKeywordAndTypes(keyword, types, pageable);
    }

    private boolean isBookmarked(final AuthPrincipal authPrincipal, final Long componentId) {
        if (!isLoggedIn(authPrincipal)) {
            return false;
        }

        final Long memberId = authPrincipal.id();
        return bookmarkRepository.existsByComponentIdAndMemberId(componentId, memberId);
    }

    private boolean isLoggedIn(final AuthPrincipal authPrincipal) {
        return authPrincipal != null;
    }
}
