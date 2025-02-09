package ject.componote.domain.component.application;

import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.dto.event.ComponentViewCountIncreaseEvent;
import ject.componote.domain.component.error.NotFoundComponentException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class ComponentViewCountEventListener {
    private final ComponentRepository componentRepository;

    @Async
    @EventListener
    @Transactional
    public void handleViewCountIncrease(final ComponentViewCountIncreaseEvent event) {
        final Long componentId = event.componentId();
        final Component component = findComponentById(componentId);
        component.increaseViewCount();
    }

    private Component findComponentById(final Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundComponentException(componentId));
    }
}
