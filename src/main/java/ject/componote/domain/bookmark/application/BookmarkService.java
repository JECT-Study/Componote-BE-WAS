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
import ject.componote.domain.bookmark.error.NotFoundMemberException;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentRepository;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.domain.DesignSystem;
import ject.componote.domain.design.domain.DesignSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ComponentRepository componentRepository;
    private final DesignSystemRepository designSystemRepository;

    @Transactional
    public BookmarkResponse addBookmark(AuthPrincipal authPrincipal, BookmarkRequest bookmarkRequest) {
    // 북마크 중복 여부 확인
    if (bookmarkRepository.existsByMemberIdAndResourceIdAndType(
        authPrincipal.id(), bookmarkRequest.id(), bookmarkRequest.type())) {
      throw new ExistedBookmarkError(
          authPrincipal.id(), bookmarkRequest.id(), bookmarkRequest.type());
        }

        Member member = findMemberOrThrow(authPrincipal.id());
        Bookmark bookmark;

        if ("component".equals(bookmarkRequest.type())) {
            Component component = findComponentOrThrow(bookmarkRequest.id());
            bookmark = Bookmark.of(member, component);
            bookmarkRepository.save(bookmark);
            return BookmarkResponse.from(bookmark, component);
        } else if ("designSystem".equals(bookmarkRequest.type())) {
            Design designSystem = findDesignSystemOrThrow(bookmarkRequest.id());
            bookmark = Bookmark.of(member, designSystem);
            bookmarkRepository.save(bookmark);
            return BookmarkResponse.from(bookmark, designSystem);
        } else {
            throw new InvalidBookmarkTypeError(bookmarkRequest.type());
        }
    }

  @Transactional(readOnly = true)
  public PageResponse<BookmarkResponse> getBookmark(
      AuthPrincipal authPrincipal, Pageable pageable, String type, String sortType) {

    Page<Bookmark> bookmarks;

    Pageable sortedPageable = applySort(pageable, type, sortType);

    if ("component".equals(type)) {
      bookmarks = bookmarkRepository.findAllByMemberIdAndType(
          authPrincipal.id(), "component", sortedPageable);
    } else if ("designSystem".equals(type)) {
      bookmarks = bookmarkRepository.findAllByMemberIdAndType(
          authPrincipal.id(), "designSystem", sortedPageable);
    } else {
      throw new InvalidBookmarkTypeError(type);
    }

    Page<BookmarkResponse> bookmarkResponsePage = bookmarks.map(bookmark -> {
      if ("component".equals(bookmark.getType())) {
        Component component = findComponentOrThrow(bookmark.getResourceId());
        return BookmarkResponse.from(bookmark, component);
      } else {
          Design designSystem = findDesignSystemOrThrow(bookmark.getResourceId());
        return BookmarkResponse.from(bookmark, designSystem);
      }
    });

    return PageResponse.from(bookmarkResponsePage);
  }

  @Transactional
  public BookmarkResponse deleteBookmark(AuthPrincipal authPrincipal, BookmarkRequest bookmarkRequest) {
    Bookmark bookmark = bookmarkRepository.findByMemberIdAndResourceIdAndType(
            authPrincipal.id(), bookmarkRequest.id(), bookmarkRequest.type())
        .orElseThrow(() -> new NotFoundBookmarkException(
            authPrincipal.id(), bookmarkRequest.id(), bookmarkRequest.type()));

    bookmarkRepository.delete(bookmark);

    if ("component".equals(bookmarkRequest.type())) {
      Component component = findComponentOrThrow(bookmarkRequest.id());
      return BookmarkResponse.from(bookmark, component);
    } else if ("designSystem".equals(bookmarkRequest.type())) {
        Design designSystem = findDesignSystemOrThrow(bookmarkRequest.id());
      return BookmarkResponse.from(bookmark, designSystem);
    } else {
      throw new InvalidBookmarkTypeError(bookmarkRequest.type());
    }
  }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(memberId));
    }

    private Component findComponentOrThrow(Long componentId) {
        return componentRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundComponentException(componentId));
    }

    private Design findDesignSystemOrThrow(Long designSystemId) {
      return designSystemRepository.findById(designSystemId)
          .orElseThrow(() -> new NotFoundDesignSystemException(designSystemId));
    }

  private Pageable applySort(Pageable pageable, String type, String sortType) {
    Sort sort;

    if ("createdAt".equals(sortType)) {
      sort = Sort.by(Sort.Order.desc("createdAt"));
    }
    else if ("name".equals(sortType) && "component".equals(type)) {
      sort = Sort.by(Sort.Order.asc("summary.title"))
          .and(Sort.by(Sort.Order.desc("createdAt")));
    } else if ("viewCount".equals(sortType) && "component".equals(type)) {
      sort = Sort.by(Sort.Order.desc("summary.viewCount"))
          .and(Sort.by(Sort.Order.desc("createdAt")));
    } else if ("commentCount".equals(sortType) && "component".equals(type)) {
      sort = Sort.by(Sort.Order.desc("commentCount"))
          .and(Sort.by(Sort.Order.desc("createdAt")));
    }

    else if ("name".equals(sortType) && "designSystem".equals(type)) {
      sort = Sort.by(Sort.Order.asc("summary.name"))
          .and(Sort.by(Sort.Order.desc("createdAt")));
    } else if ("viewCount".equals(sortType) && "designSystem".equals(type)) {
      sort = Sort.by(Sort.Order.desc("summary.viewCount"))
          .and(Sort.by(Sort.Order.desc("createdAt")));
    } else if ("recommendCount".equals(sortType) && "designSystem".equals(type)) {
      sort = Sort.by(Sort.Order.desc("summary.recommendCount"))
          .and(Sort.by(Sort.Order.desc("createdAt")));
    } else {
      sort = Sort.by(Sort.Order.desc("createdAt"));
    }

    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
  }
}
