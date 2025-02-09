package ject.componote.domain.component.dto.event;

import ject.componote.domain.component.domain.Component;

public record ComponentViewCountIncreaseEvent(Long componentId) {
    public static ComponentViewCountIncreaseEvent from(final Component component) {
        return new ComponentViewCountIncreaseEvent(component.getId());
    }
}
