package ject.componote.domain.component.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.dao.ComponentSummaryDao;
import ject.componote.domain.component.domain.ComponentType;
import ject.componote.domain.component.dto.find.request.ComponentSearchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

import static ject.componote.fixture.MemberFixture.KIM;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ComponentSearchStrategyParameterizedTest {
    @Mock
    ComponentRepository componentRepository;

    static Stream<TestInput> provideTestInputs() {
        final AuthPrincipal authPrincipal = AuthPrincipal.from(KIM.생성(1L));
        final List<ComponentType> types = List.of(ComponentType.INPUT, ComponentType.DISPLAY);
        return Stream.of(
                new TestInput(
                        "WITH_BOOKMARK_AND_FILTER",
                        new ComponentSearchRequest("keyword", types),
                        authPrincipal,
                        true,
                        "searchWithBookmarkAndTypes"
                ),
                new TestInput(
                        "WITH_BOOKMARK",
                        new ComponentSearchRequest("keyword", null),
                        authPrincipal,
                        true,
                        "searchWithBookmark"
                ),
                new TestInput(
                        "WITHOUT_BOOKMARK_AND_FILTER",
                        new ComponentSearchRequest("keyword", null),
                        null,
                        false,
                        "searchByKeyword"
                ),
                new TestInput(
                        "WITHOUT_BOOKMARK",
                        new ComponentSearchRequest("keyword", types),
                        null,
                        false,
                        "searchByKeywordWithTypes"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestInputs")
    @DisplayName("요청값에 알맞는 검색 메서드 실행")
    void testComponentSearchStrategy(final TestInput input) {
        // given
        final AuthPrincipal authPrincipal = input.authPrincipal;
        final String keyword = input.request.keyword();
        final List<ComponentType> types = input.request.types();
        final Pageable pageable = Pageable.unpaged();
        final Page<?> expect = new PageImpl<>(List.of());

        // when
        switch (input.expectedMethod) {
            case "searchWithBookmarkAndTypes" -> doReturn(expect).when(componentRepository)
                    .searchWithBookmarkAndTypes(authPrincipal.id(), keyword, types, pageable);
            case "searchWithBookmark" -> doReturn(expect).when(componentRepository)
                    .searchWithBookmark(authPrincipal.id(), keyword, pageable);
            case "searchByKeywordWithTypes" -> doReturn(expect).when(componentRepository)
                    .searchByKeywordWithTypes(keyword, types, pageable);
            case "searchByKeyword" -> doReturn(expect).when(componentRepository)
                    .searchByKeyword(keyword, pageable);
            default -> throw new IllegalStateException("Unexpected value: " + input.expectedMethod);
        }

        final Page<ComponentSummaryDao> actual = ComponentSearchStrategy.searchBy(
                authPrincipal,
                componentRepository,
                input.request,
                pageable
        );

        // then
        assertNotNull(actual);

        // 메서드 호출 여부 검증
        switch (input.expectedMethod) {
            case "searchWithBookmarkAndTypes" -> verify(componentRepository)
                    .searchWithBookmarkAndTypes(authPrincipal.id(), keyword, types, pageable);
            case "searchWithBookmark" -> verify(componentRepository)
                    .searchWithBookmark(authPrincipal.id(), keyword, pageable);
            case "searchByKeywordWithTypes" -> verify(componentRepository)
                    .searchByKeywordWithTypes(keyword, types, pageable);
            case "searchByKeyword" -> verify(componentRepository)
                    .searchByKeyword(keyword, pageable);
            default -> throw new IllegalStateException("Unexpected value: " + input.expectedMethod);
        }
    }

    static class TestInput {
        String strategyName;
        ComponentSearchRequest request;
        AuthPrincipal authPrincipal;
        boolean isLoggedIn;
        String expectedMethod;

        TestInput(final String strategyName,
                  final ComponentSearchRequest request,
                  final AuthPrincipal authPrincipal,
                  final boolean isLoggedIn,
                  final String expectedMethod) {
            this.strategyName = strategyName;
            this.request = request;
            this.authPrincipal = authPrincipal;
            this.isLoggedIn = isLoggedIn;
            this.expectedMethod = expectedMethod;
        }
    }
}