package ject.componote.domain.bookmark.api;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.bookmark.application.BookmarkService;
import ject.componote.domain.bookmark.dto.response.BookmarkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/bookmark")
@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("{componentId}")
    public ResponseEntity<BookmarkResponse> addBookmark(@Authenticated final AuthPrincipal authPrincipal, @PathVariable(value = "componentId") final Long componentId ) {
        return ResponseEntity.ok()
                .body(bookmarkService.addBookmark(authPrincipal, componentId));
    }

    @GetMapping
    public ResponseEntity<BookmarkResponse> getBookmark(@Authenticated final AuthPrincipal authPrincipal) {
        return ResponseEntity.ok()
                .body(bookmarkService.getBookmark(authPrincipal));
    }

    @DeleteMapping("{componentId}")
    public ResponseEntity<BookmarkResponse> deleteBookmark(@Authenticated final AuthPrincipal authPrincipal, @PathVariable(value = "componentId") final Long componentId ) {
        return ResponseEntity.ok()
                .body(bookmarkService.deleteBookmark(authPrincipal, componentId));
    }


}
