package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.common.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public class ComponentBookmarkStrategy implements BookmarkStrategy {
    @Override
    public BookmarkResponse create(BookmarkService service, Member member, Long id) {
        return service.createComponentBookmark(member, id);
    }

    @Override
    public PageResponse<BookmarkResponse> get(BookmarkService service, AuthPrincipal authPrincipal, Pageable pageable) {
        return service.getComponentBookmarks(authPrincipal, pageable);
    }

    @Override
    public BookmarkResponse delete(BookmarkService service, AuthPrincipal authPrincipal, Long id) {
        return service.deleteComponentBookmark(authPrincipal, id);
    }
}