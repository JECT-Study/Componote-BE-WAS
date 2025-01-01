package ject.componote.domain.bookmark.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.domain.Role;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.bookmark.application.BookmarkService;
import ject.componote.domain.bookmark.dto.request.BookmarkRequest;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import ject.componote.domain.common.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
//        @Authenticated AuthPrincipal authPrincipal,
        @Valid @RequestBody BookmarkRequest bookmarkRequest) {
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, "testUser", Role.USER);
    return ResponseEntity.ok(bookmarkService.addBookmark(authPrincipal, bookmarkRequest));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookmarkResponse>> getBookmark(
        // @Authenticated final AuthPrincipal authPrincipal,
        @PageableDefault(size = 10, page = 0) Pageable pageable,
        @RequestParam(required = false, defaultValue = "component") String type,
        @RequestParam(required = false, defaultValue = "createdAt") String sortType) {
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, "testUser", Role.USER);
        return ResponseEntity.ok(
            bookmarkService.getBookmark(authPrincipal, pageable, type, sortType));
    }

    @DeleteMapping
    public ResponseEntity<BookmarkResponse> deleteBookmark(
            //@Authenticated final AuthPrincipal authPrincipal,
            @Valid @RequestBody BookmarkRequest bookmarkRequest) {
        AuthPrincipal authPrincipal = new AuthPrincipal(1L, "testUser", Role.USER);
        return ResponseEntity.ok()
                .body(bookmarkService.deleteBookmark(authPrincipal, bookmarkRequest));
    }
}
