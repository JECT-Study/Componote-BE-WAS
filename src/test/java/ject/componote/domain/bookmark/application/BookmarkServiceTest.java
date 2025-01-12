package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dao.BookmarkRepository;
import ject.componote.domain.bookmark.domain.Bookmark;
import ject.componote.domain.bookmark.dto.request.BookmarkRequest;
import ject.componote.domain.bookmark.dto.request.BookmarkSearchRequest;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.bookmark.error.ExistedBookmarkError;
import ject.componote.domain.bookmark.error.NotFoundBookmarkException;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.fixture.BookmarkFixture;
import ject.componote.fixture.ComponentFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static ject.componote.fixture.MemberFixture.KIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

  @Mock
  BookmarkRepository bookmarkRepository;

  @Mock
  MemberRepository memberRepository;

  @Mock
  ComponentRepository componentRepository;

  @InjectMocks
  BookmarkService bookmarkService;

  Member member;
  AuthPrincipal authPrincipal;

  @BeforeEach
  public void init() {
    member = KIM.생성(1L);
    authPrincipal = AuthPrincipal.from(member);
  }

  @Test
  @DisplayName("북마크 추가 성공")
  public void addComponentBookmark_Success() {
    // given
    Component component = ComponentFixture.INPUT_COMPONENT.생성();
    final Long componentId = component.getId();
    BookmarkRequest request = new BookmarkRequest(componentId, "component");


    when(bookmarkRepository.existsByMemberIdAndResourceIdAndType(authPrincipal.id(), componentId, "component"))
            .thenReturn(false);
    when(memberRepository.findById(authPrincipal.id()))
            .thenReturn(Optional.of(member));
    when(componentRepository.findById(componentId))
            .thenReturn(Optional.of(component));

    // when
    BookmarkResponse response = bookmarkService.addBookmark(authPrincipal, request);

    // then
    assertThat(response.resourceId()).isEqualTo(componentId);  // 1L로 기대
    verify(bookmarkRepository).save(any(Bookmark.class));
  }

  @Test
  @DisplayName("북마크 추가 시 중복 에러 발생")
  public void addBookmark_ExistedBookmarkError() {
    // given
    Long componentId = 1L;
    BookmarkRequest request = new BookmarkRequest(componentId, "component");

    when(bookmarkRepository.existsByMemberIdAndResourceIdAndType(authPrincipal.id(), componentId, "component"))
        .thenReturn(true);

    // then
    assertThatThrownBy(() -> bookmarkService.addBookmark(authPrincipal, request))
        .isInstanceOf(ExistedBookmarkError.class);
  }

  @Test
  @DisplayName("북마크 조회 성공")
  public void getBookmark_Success() {
    // given
    PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
    Component component = ComponentFixture.INPUT_COMPONENT.생성();
    Bookmark bookmark = BookmarkFixture.COMPONENT_BOOKMARK.컴포넌트_북마크_생성(member, component);
    Page<Bookmark> bookmarks = new PageImpl<>(Collections.singletonList(bookmark), pageable, 1);
    BookmarkSearchRequest bookmarkSearchRequest = new BookmarkSearchRequest("component", "createdAt");

    when(bookmarkRepository.findAllByMemberIdAndType(eq(authPrincipal.id()), eq("component"), any(PageRequest.class)))
            .thenReturn(bookmarks);
    when(componentRepository.findById(bookmark.getResourceId()))
            .thenReturn(Optional.of(component));

    // when
    PageResponse<BookmarkResponse> response = bookmarkService.getBookmark(authPrincipal, pageable, bookmarkSearchRequest);

    // then
    assertThat(response.getTotalElements()).isEqualTo(1);
    verify(bookmarkRepository).findAllByMemberIdAndType(eq(authPrincipal.id()), eq("component"), any(PageRequest.class));
  }


  @Test
  @DisplayName("북마크 삭제 성공")
  public void deleteBookmark_Success() {
    // given
    Component component = ComponentFixture.INPUT_COMPONENT.생성();
    final Long componentId = component.getId();
    BookmarkRequest request = new BookmarkRequest(componentId, "component");
    Bookmark bookmark = BookmarkFixture.COMPONENT_BOOKMARK.컴포넌트_북마크_생성(member, component);

    when(componentRepository.findById(componentId))
            .thenReturn(Optional.of(component));
    when(bookmarkRepository.findByMemberIdAndResourceIdAndType(authPrincipal.id(), componentId, "component"))
            .thenReturn(Optional.of(bookmark));

    // when
    bookmarkService.deleteBookmark(authPrincipal, request);

    // then
    verify(bookmarkRepository).delete(bookmark);
  }

  @Test
  @DisplayName("북마크 삭제 시 존재하지 않을 경우 예외 발생")
  public void deleteBookmark_NotFound() {
    // given
    Long componentId = 1L;
    BookmarkRequest request = new BookmarkRequest(componentId, "component");

    when(bookmarkRepository.findByMemberIdAndResourceIdAndType(authPrincipal.id(), componentId, "component"))
        .thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> bookmarkService.deleteBookmark(authPrincipal, request))
        .isInstanceOf(NotFoundBookmarkException.class);
  }
}
