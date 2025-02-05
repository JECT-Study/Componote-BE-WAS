package ject.componote.domain.bookmark.api;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.bookmark.application.BookmarkService;
import ject.componote.domain.bookmark.dto.response.DesignBookmarkResponse;
import ject.componote.domain.common.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/bookmarks/design")
@RequiredArgsConstructor
@RestController
public class DesignBookmarkController {

    private static final String DEFAULT_SORT_PROPERTY = "createdAt";
    private final BookmarkService bookmarkService;

    @PostMapping("/{designId}")
    public ResponseEntity<DesignBookmarkResponse> addDesignBookmark(
            @Authenticated AuthPrincipal authPrincipal,
            @PathVariable Long designId) {
        return ResponseEntity.ok(bookmarkService.addDesignBookmark(authPrincipal, designId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DesignBookmarkResponse>> getDesignBookmarks(
            @Authenticated AuthPrincipal authPrincipal,
            @PageableDefault(sort = DEFAULT_SORT_PROPERTY, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(bookmarkService.getDesignBookmarks(authPrincipal, pageable));
    }

    @DeleteMapping("/{designId}")
    public ResponseEntity<DesignBookmarkResponse> deleteDesignBookmark(
            @Authenticated AuthPrincipal authPrincipal,
            @PathVariable Long designId) {
        return ResponseEntity.ok(bookmarkService.deleteDesignBookmark(authPrincipal, designId));
    }
}
