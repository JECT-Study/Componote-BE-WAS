package ject.componote.domain.component.util;

import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.dto.find.response.ComponentDetailResponse;
import ject.componote.domain.component.dto.find.response.ComponentSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static ject.componote.fixture.ComponentFixture.INPUT_COMPONENT;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ComponentMapperTest {
    @InjectMocks
    ComponentMapper componentMapper;

    @ParameterizedTest
    @DisplayName("Component를 ComponentDetailResponse로 변환")
    @ValueSource(booleans = {true, false})
    public void mapToDetailResponseWithBookmark(final boolean isBookmarked) throws Exception {
        // given
        final Component component = INPUT_COMPONENT.생성();

        // when
        final ComponentDetailResponse response = componentMapper.mapToDetailResponse(component, isBookmarked);

        // then
        assertThat(response).isNotNull();
        assertThat(response.isBookmarked()).isEqualTo(isBookmarked);
        assertThat(response.bookmarkCount()).isEqualTo(component.getBookmarkCount().getValue());
        assertThat(response.commentCount()).isEqualTo(component.getCommentCount().getValue());
        assertThat(response.title()).isEqualTo(component.getSummary().getTitle());
        assertThat(response.introduction()).isEqualTo(component.getSummary().getIntroduction());
        assertThat(response.thumbnailUrl()).isEqualTo(component.getSummary().getThumbnail().toUrl());
    }

    @Test
    @DisplayName("Component를 ComponentSearchResponse로 변환")
    public void mapToSearchResponse() throws Exception {
        // given
        final Component component = INPUT_COMPONENT.생성();

        // when
        final ComponentSearchResponse response = componentMapper.mapToSearchResponse(component);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(component.getId());
        assertThat(response.title()).isEqualTo(component.getSummary().getTitle());
    }
}