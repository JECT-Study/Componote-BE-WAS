package ject.componote.domain.bookmark.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.bookmark.application.BookmarkService;
import ject.componote.domain.bookmark.dto.request.BookmarkRequest;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.common.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/bookmarks")
@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<BookmarkResponse> addBookmark(
        @Authenticated AuthPrincipal authPrincipal,
        @Valid @RequestBody BookmarkRequest bookmarkRequest) {
    return ResponseEntity.ok(bookmarkService.addBookmark(authPrincipal, bookmarkRequest));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookmarkResponse>> getBookmarks(
            @Authenticated final AuthPrincipal authPrincipal,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "component") String type) {

        return ResponseEntity.ok(bookmarkService.getBookmarks(authPrincipal, pageable, type));
    }

    @DeleteMapping
    public ResponseEntity<BookmarkResponse> deleteBookmark(@Authenticated final AuthPrincipal authPrincipal, @Valid @RequestBody BookmarkRequest bookmarkRequest) {
        return ResponseEntity.ok()
                .body(bookmarkService.deleteBookmark(authPrincipal, bookmarkRequest));
    }
}
