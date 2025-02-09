package ject.componote.domain.component.application;

import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.dto.event.ComponentCommentCountDecreaseEvent;
import ject.componote.domain.component.dto.event.ComponentCommentCountIncreaseEvent;
import ject.componote.domain.component.error.NotFoundComponentException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class ComponentCommentCountEventListener {
    private final ComponentRepository componentRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT) // cf) Spirng은 내부적으로 트랜잭션 정보를 ThreadLocal 변수에 저장하기 때문에 다른 쓰레드로 트랜잭션이 전파되지 않는다.
    public void handleCommentCountIncreaseEvent(final ComponentCommentCountIncreaseEvent event) {
        final Long componentId = event.componentId();
        final Component component = findComponentById(componentId);
        component.increaseCommentCount();
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
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
