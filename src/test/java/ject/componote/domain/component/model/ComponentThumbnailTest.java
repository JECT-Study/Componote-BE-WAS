package ject.componote.domain.component.model;

import ject.componote.domain.component.error.InvalidComponentThumbnailExtensionException;
import ject.componote.domain.component.error.NotFoundComponentThumbnailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ComponentThumbnailTest {
    @ParameterizedTest(name = "값: {0}")
    @DisplayName("확장자가 잘못된 경우")
    @ValueSource(strings = {"hello.gif", "hello", "hello.jqp"})
    public void invalidExtension(final String objectKey) {
        assertThatThrownBy(() -> ComponentThumbnail.from(objectKey))
                .isInstanceOf(InvalidComponentThumbnailExtensionException.class);
    }

    @Test
    @DisplayName("objectKey가 전달되지 않으면 예외 발생")
    public void createDefault() {
        // given
        final String objectKey = null;

        // then
        assertThatThrownBy(() -> ComponentThumbnail.from(objectKey))
                .isInstanceOf(NotFoundComponentThumbnailException.class);
    }
}