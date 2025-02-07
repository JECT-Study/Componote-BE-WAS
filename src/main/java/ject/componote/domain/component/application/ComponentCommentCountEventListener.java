package ject.componote.domain.component.application;

import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.dto.event.ComponentCommentCountDecreaseEvent;
import ject.componote.domain.component.dto.event.ComponentCommentCountIncreaseEvent;
import ject.componote.domain.component.error.NotFoundComponentException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class ComponentCommentCountEventListener {
    private final ComponentRepository componentRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentCountIncreaseEvent(final ComponentCommentCountIncreaseEvent event) {
        final Long componentId = event.componentId();
        final Component component = findComponentById(componentId);
        component.increaseCommentCount();
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentCountDecreaseEvent(final ComponentCommentCountDecreaseEvent event) {
        final Long componentId = event.componentId();
        final Component component = findComponentById(componentId);
        component.decreaseCommentCount();
    }

    private Component findComponentById(final Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundComponentException(componentId));
    }
}
