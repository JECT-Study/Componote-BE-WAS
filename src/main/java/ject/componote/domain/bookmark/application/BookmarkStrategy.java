package ject.componote.domain.bookmark.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.common.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface BookmarkStrategy {
    BookmarkResponse create(BookmarkService service, Member member, Long id);
    PageResponse<BookmarkResponse> get(BookmarkService service, AuthPrincipal authPrincipal, Pageable pageable);
    BookmarkResponse delete(BookmarkService service, AuthPrincipal authPrincipal, Long id);
}