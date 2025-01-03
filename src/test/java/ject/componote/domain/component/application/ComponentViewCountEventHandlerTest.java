package ject.componote.domain.component.application;

import ject.componote.domain.common.model.Count;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.dto.find.event.ComponentViewCountIncreaseEvent;
import ject.componote.domain.component.error.NotFoundComponentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static ject.componote.fixture.ComponentFixture.INPUT_COMPONENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ComponentViewCountEventHandlerTest {
    @Mock
    ComponentRepository componentRepository;

    @InjectMocks
    ComponentViewCountEventHandler componentViewCountEventHandler;

    Component component = INPUT_COMPONENT.생성();

    @Test
    @DisplayName("조회 수 증가")
    public void handleViewCountIncrease() throws Exception {
        // given
        final ComponentViewCountIncreaseEvent event = ComponentViewCountIncreaseEvent.from(component);
        final Long componentId = component.getId();
        final Count previousViewCount = component.getViewCount();

        // when
        doReturn(Optional.of(component)).when(componentRepository)
                .findById(componentId);
        componentViewCountEventHandler.handleViewCountIncrease(event);

        // then
        final Count newViewCount = component.getViewCount();
        previousViewCount.increase();
        assertThat(previousViewCount).isEqualTo(newViewCount);
    }

    @Test
    @DisplayName("조회 수 증가 시 componentId 가 잘못된 경우 예외 발생")
    public void handleViewCountIncreaseWhenInvalidComponentId() throws Exception {
        // given
        final ComponentViewCountIncreaseEvent event = ComponentViewCountIncreaseEvent.from(component);
        final Long componentId = component.getId();

        // when
        doReturn(Optional.empty()).when(componentRepository)
                .findById(componentId);

        // then
        assertThatThrownBy(() -> componentViewCountEventHandler.handleViewCountIncrease(event))
                .isInstanceOf(NotFoundComponentException.class);

    }
}