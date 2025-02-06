//package ject.componote.domain.design;
//
//import ject.componote.domain.auth.model.AuthPrincipal;
//import ject.componote.domain.common.dto.response.PageResponse;
//import ject.componote.domain.design.application.DesignSystemService;
//import ject.componote.domain.design.dao.DesignSystemRepository;
//import ject.componote.domain.design.domain.Design;
//import ject.componote.domain.design.dto.search.request.DesignSystemSearchRequest;
//import ject.componote.domain.design.dto.search.response.DesignSystemSearchResponse;
//import ject.componote.fixture.DesignFixture;
//import ject.componote.fixture.MemberFixture;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Stream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class DesignSystemServiceTest {
//
//    @Mock
//    DesignSystemRepository designSystemRepository;
//
//    @InjectMocks
//    DesignSystemService designSystemService;
//
//    AuthPrincipal authPrincipal;
//
//    @BeforeEach
//    public void init() {
//        authPrincipal = AuthPrincipal.from(MemberFixture.KIM.생성(1L));
//    }
//
//    static Stream<SearchInput> provideSearchInputs() {
//        final AuthPrincipal authPrincipal = AuthPrincipal.from(MemberFixture.KIM.생성(1L));
//        return Stream.of(
//                new SearchInput("비회원", null, "검색어"),
//                new SearchInput("회원", authPrincipal, "검색어")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideSearchInputs")
//    @DisplayName("디자인 시스템 검색 성공")
//    public void searchDesignSystem_Success(final SearchInput input) {
//        // given
//        final Pageable pageable = PageRequest.of(0, 10);
//        final Design design = DesignFixture.기본_디자인_생성();
//        final Page<Design> designs = new PageImpl<>(List.of(design), pageable, 1);
//        final DesignSystemSearchRequest request = new DesignSystemSearchRequest(input.keyword, null);
//
//        when(designSystemRepository.findByKeyword(anyString(), any(Pageable.class)))
//                .thenReturn(designs);
//
//        // when
//        PageResponse<DesignSystemSearchResponse> response = designSystemService.searchDesignSystem(input.authPrincipal, request, pageable);
//
//        // then
//        assertThat(response.getTotalElements()).isEqualTo(1);
//
//        // ✅ 실제로 해당 메서드가 호출되었는지 검증
//        verify(designSystemRepository).findByKeyword(anyString(), any(Pageable.class));
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideSearchInputs")
//    @DisplayName("디자인 시스템 검색 - 검색 결과 없음")
//    public void searchDesignSystem_EmptyResult(final SearchInput input) {
//        // given
//        final Pageable pageable = PageRequest.of(0, 10);
//        final DesignSystemSearchRequest request = new DesignSystemSearchRequest(input.keyword, null);
//        final Page<Design> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
//
//        when(designSystemRepository.findByKeyword(anyString(), any(Pageable.class)))
//                .thenReturn(emptyPage);
//
//        // when
//        PageResponse<DesignSystemSearchResponse> response = designSystemService.searchDesignSystem(input.authPrincipal, request, pageable);
//
//        // then
//        assertThat(response.getTotalElements()).isEqualTo(0);
//        verify(designSystemRepository).findByKeyword(anyString(), any(Pageable.class));
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideSearchInputs")
//    @DisplayName("디자인 시스템 검색 - 필터 적용 후 검색")
//    public void searchDesignSystem_WithFilters(final SearchInput input) {
//        // given
//        final Pageable pageable = PageRequest.of(0, 10);
//        final List<Long> filteredDesignIds = List.of(1L, 2L);
//        final DesignSystemSearchRequest request = new DesignSystemSearchRequest(input.keyword, List.of());
//        final Page<Design> designs = new PageImpl<>(List.of(DesignFixture.기본_디자인_생성()), pageable, 1);
//
//        when(designSystemRepository.findAllByIdIn(anyList(), any(Pageable.class)))
//                .thenReturn(designs);
//
//        // when
//        PageResponse<DesignSystemSearchResponse> response = designSystemService.searchDesignSystem(input.authPrincipal, request, pageable);
//
//        // then
//        assertThat(response.getTotalElements()).isEqualTo(1);
//        verify(designSystemRepository).findAllByIdIn(anyList(), any(Pageable.class));
//    }
//
//    static class SearchInput {
//        String displayName;
//        AuthPrincipal authPrincipal;
//        String keyword;
//
//        public SearchInput(final String displayName,
//                           final AuthPrincipal authPrincipal,
//                           final String keyword) {
//            this.displayName = displayName;
//            this.authPrincipal = authPrincipal;
//            this.keyword = keyword;
//        }
//    }
//}
