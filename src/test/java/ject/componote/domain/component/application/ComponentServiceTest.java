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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ject.componote.fixture.ComponentFixture.INPUT_COMPONENT;
import static ject.componote.fixture.MemberFixture.KIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ComponentServiceTest {
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Mock
    BookmarkRepository bookmarkRepository;

    @Mock
    ComponentMapper componentMapper;

    @Mock
    ComponentRepository componentRepository;

    @InjectMocks
    ComponentService componentService;

    Component component = INPUT_COMPONENT.생성();

    static Stream<DetailInput> provideDetailInputs() {
        final AuthPrincipal authPrincipal = AuthPrincipal.from(KIM.생성(1L));
        return Stream.of(
                new DetailInput("비회원", null, false),
                new DetailInput("회원 (북마크 X)", authPrincipal, false),
                new DetailInput("회원 (북마크 O)", authPrincipal, true)
        );
    }

    static Stream<SearchInput> provideSearchInputs() {
        final AuthPrincipal authPrincipal = AuthPrincipal.from(KIM.생성(1L));
        return Stream.of(
                new SearchInput("비회원", null, "검색어", List.of(ComponentType.INPUT)),
                new SearchInput("회원", authPrincipal, "검색어", List.of(ComponentType.INPUT))
        );
    }

    @ParameterizedTest
    @MethodSource("provideDetailInputs")
    @DisplayName("컴포넌트 상세 조회")
    public void getComponentDetail(final DetailInput input) throws Exception {
        // given
        final Long componentId = component.getId();
        final ComponentViewCountIncreaseEvent event = ComponentViewCountIncreaseEvent.from(component);
        final ComponentDetailResponse expect = new ComponentDetailResponse(
                component.getSummary().getTitle(),
                Collections.emptyList(),
                component.getSummary().getIntroduction(),
                component.getCommentCount().getValue(),
                component.getBookmarkCount().getValue(),
                component.getDesignReferenceCount().getValue(),
                component.getSummary().getThumbnail().toUrl(),
                Collections.emptyMap(),
                input.isBookmarked
        );

        // when
        doReturn(Optional.of(component)).when(componentRepository)
                .findById(componentId);
        doNothing().when(eventPublisher)
                .publishEvent(event);
        doReturn(expect).when(componentMapper)
                .mapFrom(component, input.isBookmarked);
        if (input.isBookmarked) {
            doReturn(true).when(bookmarkRepository)
                    .existsByComponentIdAndMemberId(componentId, input.authPrincipal.id());
        }

        final ComponentDetailResponse actual = componentService.getComponentDetail(input.authPrincipal, componentId);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @ParameterizedTest
    @MethodSource("provideDetailInputs")
    @DisplayName("컴포넌트 상세 조회 시 componentId 가 잘못된 경우 예외 발생")
    public void getComponentDetailWhenInvalidComponentId(final DetailInput input) throws Exception {
        // given
        final Long componentId = component.getId();

        // when
        doReturn(Optional.empty()).when(componentRepository)
                .findById(componentId);

        // then
        assertThatThrownBy(() -> componentService.getComponentDetail(input.authPrincipal, componentId))
                .isInstanceOf(NotFoundComponentException.class);
    }

    @ParameterizedTest
    @MethodSource("provideSearchInputs")
    @DisplayName("컴포넌트 검색")
    public void search(final SearchInput input) throws Exception {
        // given
        final AuthPrincipal authPrincipal = input.authPrincipal;
        final String keyword = input.keyword;
        final List<ComponentType> types = input.types;
        final ComponentSearchRequest request = input.toRequest();
        final Pageable pageable = input.pageable;
        final Page<ComponentSummaryDao> page = new PageImpl<>(Collections.emptyList(), pageable, 0L);

        // when
        switch (input.displayName) {
            case "비회원" -> doReturn(page).when(componentRepository)
                    .findAllByKeywordAndTypes(keyword, types, pageable);
            case "회원" -> doReturn(page).when(componentRepository)
                    .findAllByKeywordAndTypesWithBookmark(authPrincipal.id(), keyword, types, pageable);
            default -> throw new IllegalStateException("Unexpected value: " + input.displayName);
        }

        final PageResponse<ComponentSummaryResponse> actual = componentService.search(authPrincipal, request, pageable);

        // then
        assertThat(actual).isNotNull();
        switch (input.displayName) {
            case "비회원" -> verify(componentRepository)
                    .findAllByKeywordAndTypes(keyword, types, pageable);
            case "회원" -> verify(componentRepository)
                    .findAllByKeywordAndTypesWithBookmark(authPrincipal.id(), keyword, types, pageable);
            default -> throw new IllegalStateException("Unexpected value: " + input.displayName);
        }
    }

    static class DetailInput {
        String displayName;
        AuthPrincipal authPrincipal;
        boolean isBookmarked;

        public DetailInput(final String displayName,
                           final AuthPrincipal authPrincipal,
                           final boolean isBookmarked) {
            this.displayName = displayName;
            this.authPrincipal = authPrincipal;
            this.isBookmarked = isBookmarked;
        }
    }

    static class SearchInput {
        String displayName;
        AuthPrincipal authPrincipal;
        String keyword;
        List<ComponentType> types;
        Pageable pageable;

        public SearchInput(final String displayName,
                           final AuthPrincipal authPrincipal,
                           final String keyword,
                           final List<ComponentType> types) {
            this.displayName = displayName;
            this.authPrincipal = authPrincipal;
            this.keyword = keyword;
            this.types = types;
            this.pageable = Pageable.unpaged();
        }

        public ComponentSearchRequest toRequest() {
            return new ComponentSearchRequest(keyword, types);
        }
    }
}