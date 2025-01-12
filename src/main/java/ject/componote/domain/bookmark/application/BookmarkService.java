package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dao.ComponentBookmarkRepository;
import ject.componote.domain.bookmark.dao.DesignSystemBookmarkRepository;
import ject.componote.domain.bookmark.domain.ComponentBookmark;
import ject.componote.domain.bookmark.domain.DesignSystemBookmark;
import ject.componote.domain.bookmark.dto.request.BookmarkRequest;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.bookmark.error.ExistedBookmarkError;
import ject.componote.domain.bookmark.error.InvalidBookmarkTypeError;
import ject.componote.domain.bookmark.error.NotFoundBookmarkException;
import ject.componote.domain.bookmark.error.NotFoundComponentException;
import ject.componote.domain.bookmark.error.NotFoundDesignSystemException;
import ject.componote.domain.bookmark.error.NotFoundMemberException;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.dao.DesignSystemRepository;
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

    private final MemberRepository memberRepository;
    private final ComponentRepository componentRepository;
    private final DesignSystemRepository designSystemRepository;
    private final ComponentBookmarkRepository componentBookmarkRepository;
    private final DesignSystemBookmarkRepository designSystemBookmarkRepository;

    @Transactional
    public BookmarkResponse addBookmark(AuthPrincipal authPrincipal, BookmarkRequest request) {
        Member member = findMemberOrThrow(authPrincipal.id());

        if ("component".equals(request.type())) {
            if (componentBookmarkRepository.existsByMemberIdAndComponentId(member.getId(), request.id())) {
                throw new ExistedBookmarkError(member.getId(), request.id(), "component");
            }
            return createComponentBookmark(member, request.id());

        } else if ("designSystem".equals(request.type())) {
            if (designSystemBookmarkRepository.existsByMemberIdAndDesignId(member.getId(), request.id())) {
                throw new ExistedBookmarkError(member.getId(), request.id(), "designSystem");
            }
            return createDesignSystemBookmark(member, request.id());

        } else {
            throw new InvalidBookmarkTypeError(request.type());
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<BookmarkResponse> getBookmarks(AuthPrincipal authPrincipal, Pageable pageable, String type) {
        if ("component".equals(type)) {
            return getComponentBookmarks(authPrincipal, pageable);
        } else if ("designSystem".equals(type)) {
            return getDesignSystemBookmarks(authPrincipal, pageable);
        } else {
            throw new InvalidBookmarkTypeError(type);
        }
    }

    @Transactional
    public BookmarkResponse deleteBookmark(AuthPrincipal authPrincipal, BookmarkRequest request) {
        if ("component".equals(request.type())) {
            return deleteComponentBookmark(authPrincipal, request.id());
        } else if ("designSystem".equals(request.type())) {
            return deleteDesignSystemBookmark(authPrincipal, request.id());
        } else {
            throw new InvalidBookmarkTypeError(request.type());
        }
    }

    private BookmarkResponse createComponentBookmark(Member member, Long componentId) {
        if (componentBookmarkRepository.existsByMemberIdAndComponentId(member.getId(), componentId)) {
            throw new ExistedBookmarkError(member.getId(), componentId, "component");
        }
        Component component = componentRepository.findById(componentId)
                .orElseThrow(() -> new IllegalArgumentException("Component not found"));
        ComponentBookmark componentBookmark = ComponentBookmark.of(member, component);
        componentBookmarkRepository.save(componentBookmark);
        return BookmarkResponse.from(componentBookmark, component);
    }

    private BookmarkResponse createDesignSystemBookmark(Member member, Long designId) {
        if (designSystemBookmarkRepository.existsByMemberIdAndDesignId(member.getId(), designId)) {
            throw new ExistedBookmarkError(member.getId(), designId, "designSystem");
        }
        Design design = designSystemRepository.findById(designId)
                .orElseThrow(() -> new IllegalArgumentException("DesignSystem not found"));
        DesignSystemBookmark designSystemBookmark = DesignSystemBookmark.of(member, design);
        designSystemBookmarkRepository.save(designSystemBookmark);
        return BookmarkResponse.from(designSystemBookmark, design);
    }

    private PageResponse<BookmarkResponse> getComponentBookmarks(AuthPrincipal authPrincipal, Pageable pageable) {
        Page<ComponentBookmark> bookmarks = componentBookmarkRepository.findAllByMemberId(authPrincipal.id(), pageable);
        return PageResponse.from(bookmarks.map(bookmark -> {
            Component component = componentRepository.findById(bookmark.getComponentId())
                    .orElseThrow(() -> new IllegalArgumentException("Component not found"));
            return BookmarkResponse.from(bookmark, component);
        }));
    }

    private PageResponse<BookmarkResponse> getDesignSystemBookmarks(AuthPrincipal authPrincipal, Pageable pageable) {
        Page<DesignSystemBookmark> bookmarks = designSystemBookmarkRepository.findAllByMemberId(authPrincipal.id(), pageable);
        return PageResponse.from(bookmarks.map(bookmark -> {
            Design design = designSystemRepository.findById(bookmark.getDesignId())
                    .orElseThrow(() -> new IllegalArgumentException("DesignSystem not found"));
            return BookmarkResponse.from(bookmark, design);
        }));
    }

    private BookmarkResponse deleteComponentBookmark(AuthPrincipal authPrincipal, Long componentId) {
        ComponentBookmark bookmark = componentBookmarkRepository.findByMemberIdAndComponentId(authPrincipal.id(), componentId)
                .orElseThrow(() -> new NotFoundBookmarkException(authPrincipal.id(), componentId, "component"));
        componentBookmarkRepository.delete(bookmark);
        Component component = componentRepository.findById(bookmark.getComponentId())
                .orElseThrow(() -> new IllegalArgumentException("Component not found"));
        return BookmarkResponse.from(bookmark, component);
    }

    private BookmarkResponse deleteDesignSystemBookmark(AuthPrincipal authPrincipal, Long designId) {
        DesignSystemBookmark bookmark = designSystemBookmarkRepository.findByMemberIdAndDesignId(authPrincipal.id(), designId)
                .orElseThrow(() -> new NotFoundBookmarkException(authPrincipal.id(), designId, "designSystem"));
        designSystemBookmarkRepository.delete(bookmark);
        Design design = designSystemRepository.findById(bookmark.getDesignId())
                .orElseThrow(() -> new IllegalArgumentException("DesignSystem not found"));
        return BookmarkResponse.from(bookmark, design);
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
