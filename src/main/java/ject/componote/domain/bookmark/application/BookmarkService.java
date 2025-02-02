package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dao.ComponentBookmarkRepository;
import ject.componote.domain.bookmark.dao.DesignBookmarkRepository;
import ject.componote.domain.bookmark.domain.ComponentBookmark;
import ject.componote.domain.bookmark.domain.DesignBookmark;
import ject.componote.domain.bookmark.dto.request.BookmarkRequest;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.bookmark.error.NotFoundBookmarkException;
import ject.componote.domain.bookmark.error.NotFoundComponentException;
import ject.componote.domain.bookmark.error.NotFoundDesignSystemException;
import ject.componote.domain.bookmark.error.NotFoundMemberException;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.domain.component.dao.ComponentRepository;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.design.dao.DesignSystemRepository;
import ject.componote.domain.design.domain.Design;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final MemberRepository memberRepository;
    private final ComponentRepository componentRepository;
    private final DesignSystemRepository designSystemRepository;
    private final ComponentBookmarkRepository componentBookmarkRepository;
    private final DesignBookmarkRepository designBookmarkRepository;

    @Transactional
    public BookmarkResponse addBookmark(AuthPrincipal authPrincipal, BookmarkRequest request) {
        Member member = findMemberOrThrow(authPrincipal.id());
        BookmarkType bookmarkType = BookmarkType.from(request.type());
        return bookmarkType.getStrategy().create(this, member, request.id());
    }


    @Transactional(readOnly = true)
    public PageResponse<BookmarkResponse> getBookmarks(AuthPrincipal authPrincipal, Pageable pageable, String type) {
        BookmarkType bookmarkType = BookmarkType.from(type);
        return bookmarkType.getStrategy().get(this, authPrincipal, pageable);
    }

    @Transactional
    public BookmarkResponse deleteBookmark(AuthPrincipal authPrincipal, BookmarkRequest request) {
        BookmarkType bookmarkType = BookmarkType.from(request.type());
        return bookmarkType.getStrategy().delete(this, authPrincipal, request.id());
    }

    public BookmarkResponse createComponentBookmark(Member member, Long componentId) {
        Component component = componentRepository.findById(componentId)
                .orElseThrow(() -> new NotFoundComponentException(componentId));
        ComponentBookmark bookmark = componentBookmarkRepository.save(ComponentBookmark.of(member, component));
        return BookmarkResponse.from(bookmark, component);
    }

    public BookmarkResponse createDesignSystemBookmark(Member member, Long designId) {
        Design design = designSystemRepository.findById(designId)
                .orElseThrow(() -> new NotFoundDesignSystemException(designId));
        DesignBookmark bookmark = designBookmarkRepository.save(DesignBookmark.of(member, design));
        return BookmarkResponse.from(bookmark, design);
    }

    public PageResponse<BookmarkResponse> getComponentBookmarks(AuthPrincipal authPrincipal, Pageable pageable) {
        Page<ComponentBookmark> bookmarks = componentBookmarkRepository.findAllByMemberId(authPrincipal.id(), pageable);
        return PageResponse.from(bookmarks.map(bookmark -> {
            Component component = componentRepository.findById(bookmark.getComponentId())
                    .orElseThrow(() -> new NotFoundComponentException(bookmark.getComponentId()));
            return BookmarkResponse.from(bookmark, component);
        }));
    }

    public PageResponse<BookmarkResponse> getDesignSystemBookmarks(AuthPrincipal authPrincipal, Pageable pageable) {
        Page<DesignBookmark> bookmarks = designBookmarkRepository.findAllByMemberId(authPrincipal.id(), pageable);
        return PageResponse.from(bookmarks.map(bookmark -> {
            Design design = designSystemRepository.findById(bookmark.getDesignId())
                    .orElseThrow(() -> new NotFoundDesignSystemException(bookmark.getDesignId()));
            return BookmarkResponse.from(bookmark, design);
        }));
    }

    public BookmarkResponse deleteComponentBookmark(AuthPrincipal authPrincipal, Long componentId) {
        ComponentBookmark bookmark = componentBookmarkRepository.findByMemberIdAndComponentId(authPrincipal.id(), componentId)
                .orElseThrow(() -> new NotFoundBookmarkException(authPrincipal.id(), componentId, "component"));
        componentBookmarkRepository.delete(bookmark);
        Component component = componentRepository.findById(bookmark.getComponentId())
                .orElseThrow(() -> new NotFoundComponentException(bookmark.getComponentId()));
        return BookmarkResponse.from(bookmark, component);
    }

    public BookmarkResponse deleteDesignSystemBookmark(AuthPrincipal authPrincipal, Long designId) {
        DesignBookmark bookmark = designBookmarkRepository.findByMemberIdAndDesignId(authPrincipal.id(), designId)
                .orElseThrow(() -> new NotFoundBookmarkException(authPrincipal.id(), designId, "designSystem"));
        designBookmarkRepository.delete(bookmark);
        Design design = designSystemRepository.findById(bookmark.getDesignId())
                .orElseThrow(() -> new NotFoundDesignSystemException(bookmark.getDesignId()));
        return BookmarkResponse.from(bookmark, design);
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException(memberId));
    }
}
