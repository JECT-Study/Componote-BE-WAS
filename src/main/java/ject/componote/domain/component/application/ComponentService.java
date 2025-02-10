package ject.componote.domain.component.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dao.ComponentBookmarkRepository;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentType;
import ject.componote.domain.component.dto.event.ComponentViewCountIncreaseEvent;
import ject.componote.domain.component.dto.find.request.ComponentSearchRequest;
import ject.componote.domain.component.dto.find.request.ComponentSummaryRequest;
import ject.componote.domain.component.dto.find.response.ComponentDetailResponse;
import ject.componote.domain.component.dto.find.response.ComponentSearchResponse;
import ject.componote.domain.component.dto.find.response.ComponentSummaryResponse;
import ject.componote.domain.component.error.NotFoundComponentException;
import ject.componote.domain.component.util.ComponentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComponentService {
    private final ApplicationEventPublisher eventPublisher;
    private final ComponentMapper componentMapper;
    private final ComponentRepository componentRepository;
    private final ComponentBookmarkRepository componentBookmarkRepository;

    public ComponentDetailResponse getComponentDetail(final AuthPrincipal authPrincipal, final Long componentId) {
        final Component component = findComponentById(componentId);
        eventPublisher.publishEvent(ComponentViewCountIncreaseEvent.from(component));
        return componentMapper.mapToDetailResponse(component, isBookmarked(authPrincipal, componentId));
    }

    public PageResponse<ComponentSummaryResponse> getAllComponentSummaries(final AuthPrincipal authPrincipal,
                                                                           final ComponentSummaryRequest request,
                                                                           final Pageable pageable) {
        final List<ComponentType> types = request.types();
        final Set<Long> componentIds = componentRepository.findAllComponentIdsByTypes(types, pageable);
        final Set<Long> bookmarkedComponentIds = findBookmarkedComponentIds(authPrincipal, componentIds);
        final Page<ComponentSummaryResponse> page = componentRepository.findAllByComponentIdsAndTypes(componentIds, types, pageable)
                .map(component -> ComponentSummaryResponse.of(component, bookmarkedComponentIds.contains(component.getId())));
        return PageResponse.from(page);
    }

    public PageResponse<ComponentSearchResponse> search(final ComponentSearchRequest request,
                                                        final Pageable pageable) {
        final String keyword = request.keyword();
        final Set<Long> componentIds = componentRepository.findAllComponentIdsByKeyword(keyword, pageable); // 굳이 이 메서드를 외부에 제공할 필요가 없긴 하나, 추후 북마크 등 필드 추가 대비
        final Page<ComponentSearchResponse> page = componentRepository.findAllByComponentIdsAndKeyword(componentIds, keyword, pageable)
                .map(componentMapper::mapToSearchResponse);
        return PageResponse.from(page);
    }

    private Component findComponentById(final Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundComponentException(componentId));
    }

    private Set<Long> findBookmarkedComponentIds(final AuthPrincipal authPrincipal, final Set<Long> componentIds) {
        if (!isLoggedIn(authPrincipal)) {
            return Collections.emptySet();
        }
        return componentBookmarkRepository.findAllComponentIdsByMemberIdAndComponentIds(authPrincipal.id(), componentIds);
    }

    private boolean isBookmarked(final AuthPrincipal authPrincipal, final Long componentId) {
        if (!isLoggedIn(authPrincipal)) {
            return false;
        }
        return componentBookmarkRepository.existsByMemberIdAndComponentId(authPrincipal.id(), componentId);
    }

    private boolean isLoggedIn(final AuthPrincipal authPrincipal) {
        return authPrincipal != null;
    }
}
