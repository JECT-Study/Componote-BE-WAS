package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.domain.Bookmark;
import ject.componote.domain.bookmark.domain.BookmarkRepository;
import ject.componote.domain.bookmark.dto.request.BookmarkRequest;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.bookmark.error.ExistedBookmarkError;
import ject.componote.domain.bookmark.error.InvalidBookmarkTypeError;
import ject.componote.domain.bookmark.error.NotFoundBookmarkException;
import ject.componote.domain.bookmark.error.NotFoundComponentException;
import ject.componote.domain.bookmark.error.NotFoundDesignSystemException;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentRepository;
import ject.componote.domain.design.domain.DesignSystem;
import ject.componote.domain.design.domain.DesignSystemRepository;
import ject.componote.fixture.BookmarkFixture;
import ject.componote.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static ject.componote.fixture.MemberFixture.KIM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

  @Mock
  BookmarkRepository bookmarkRepository;

  @Mock
  MemberRepository memberRepository;

  @Mock
  ComponentRepository componentRepository;

  @Mock
  DesignSystemRepository designSystemRepository;

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
  @DisplayName("Component 북마크 추가 성공")
  public void addComponentBookmark_Success() {
    // given
    Long componentId = 1L;
    BookmarkRequest request = new BookmarkRequest(componentId, "component");

    // Fixture 사용
    Component component = ComponentFixture.기본_컴포넌트_생성(componentId);
    Bookmark bookmark = BookmarkFixture.COMPONENT_BOOKMARK.컴포넌트_북마크_생성(member, component);

    when(bookmarkRepository.existsByMemberIdAndResourceIdAndType(authPrincipal.id(), componentId, "component"))
        .thenReturn(false);
    when(memberRepository.findById(authPrincipal.id()))
        .thenReturn(Optional.of(member));
    when(componentRepository.findById(componentId))
        .thenReturn(Optional.of(component));

    // when
    BookmarkResponse response = bookmarkService.addBookmark(authPrincipal, request);

    // then
    assertThat(response.resourceId()).isEqualTo(componentId);
    verify(bookmarkRepository).save(any(Bookmark.class));
  }

  @Test
  @DisplayName("DesignSystem 북마크 추가 성공")
  public void addDesignSystemBookmark_Success() {
    // given
    Long designSystemId = 1L;
    BookmarkRequest request = new BookmarkRequest(designSystemId, "designSystem");
    DesignSystem designSystem = new DesignSystem();

    when(bookmarkRepository.existsByMemberIdAndResourceIdAndType(authPrincipal.id(), designSystemId, "designSystem"))
        .thenReturn(false);
    when(memberRepository.findById(authPrincipal.id()))
        .thenReturn(Optional.of(member));
    when(designSystemRepository.findById(designSystemId))
        .thenReturn(Optional.of(designSystem));

    // when
    BookmarkResponse response = bookmarkService.addBookmark(authPrincipal, request);

    // then
    assertThat(response.resourceId()).isEqualTo(designSystemId);
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
    PageRequest pageable = PageRequest.of(0, 10);
    Bookmark bookmark = new Bookmark(member.getId(), 1L, "component");
    Page<Bookmark> bookmarks = new PageImpl<>(Collections.singletonList(bookmark), pageable, 1);

    when(bookmarkRepository.findAllByMemberIdAndType(authPrincipal.id(), "component", pageable))
        .thenReturn(bookmarks);
    when(componentRepository.findById(bookmark.getResourceId()))
        .thenReturn(Optional.of(new Component()));

    // when
    PageResponse<BookmarkResponse> response = bookmarkService.getBookmark(authPrincipal, pageable, "component", "createdAt");

    // then
    assertThat(response.getTotalCount()).isEqualTo(1);
    verify(bookmarkRepository).findAllByMemberIdAndType(authPrincipal.id(), "component", pageable);
  }

  @Test
  @DisplayName("북마크 삭제 성공")
  public void deleteBookmark_Success() {
    // given
    Long componentId = 1L;
    BookmarkRequest request = new BookmarkRequest(componentId, "component");
    Bookmark bookmark = new Bookmark(member.getId(), componentId, "component");

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
