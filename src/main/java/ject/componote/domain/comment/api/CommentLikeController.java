package ject.componote.domain.comment.api;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.auth.model.User;
import ject.componote.domain.comment.application.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/comment-likes/{commentId}")
@RequiredArgsConstructor
@RestController
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping
    @User
    public ResponseEntity<Void> likeComment(@Authenticated final AuthPrincipal authPrincipal,
                                            @PathVariable("commentId") final Long commentId) {
        commentLikeService.likeComment(authPrincipal, commentId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping
    @User
    public ResponseEntity<Void> unlikeComment(@Authenticated final AuthPrincipal authPrincipal,
                                              @PathVariable("commentId") final Long commentId) {
        commentLikeService.unlikeComment(authPrincipal, commentId);
        return ResponseEntity.noContent()
                .build();
    }
}
