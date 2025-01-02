package ject.componote.domain.component.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dao.BookmarkRepository;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.dto.find.event.ComponentViewCountIncreaseEvent;
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
        final Page<ComponentSummaryResponse> page = ComponentSearchStrategy.searchBy(authPrincipal, componentRepository, request, pageable)
                .map(ComponentSummaryResponse::from);
        return PageResponse.from(page);
    }

    private Component findComponentById(final Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundComponentException(componentId));
    }

    private boolean isBookmarked(final AuthPrincipal authPrincipal, final Long componentId) {
        if (authPrincipal == null) {
            return false;
        }

        final Long memberId = authPrincipal.id();
        return bookmarkRepository.existsByComponentIdAndMemberId(componentId, memberId);
    }
}
