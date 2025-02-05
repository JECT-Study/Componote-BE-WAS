package ject.componote.domain.bookmark.api;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.bookmark.application.BookmarkService;
import ject.componote.domain.bookmark.dto.response.ComponentBookmarkResponse;
import ject.componote.domain.common.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/bookmarks/component")
@RequiredArgsConstructor
@RestController
public class ComponentBookmarkController {

    private static final String DEFAULT_SORT_PROPERTY = "createdAt";
    private final BookmarkService bookmarkService;

    @PostMapping("/{componentId}")
    public ResponseEntity<ComponentBookmarkResponse> addComponentBookmark(
            @Authenticated AuthPrincipal authPrincipal,
            @PathVariable Long componentId) {
        return ResponseEntity.ok(bookmarkService.addComponentBookmark(authPrincipal, componentId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ComponentBookmarkResponse>> getComponentBookmarks(
            @Authenticated AuthPrincipal authPrincipal,
            @PageableDefault(sort = DEFAULT_SORT_PROPERTY, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(bookmarkService.getComponentBookmarks(authPrincipal, pageable));
    }

    @DeleteMapping("/{componentId}")
    public ResponseEntity<ComponentBookmarkResponse> deleteComponentBookmark(
            @Authenticated AuthPrincipal authPrincipal,
            @PathVariable Long componentId) {
        return ResponseEntity.ok(bookmarkService.deleteComponentBookmark(authPrincipal, componentId));
    }
}
