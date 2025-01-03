package ject.componote.domain.comment.api;

import jakarta.validation.Valid;
import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.auth.model.Authenticated;
import ject.componote.domain.auth.model.User;
import ject.componote.domain.comment.application.CommentService;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;
import ject.componote.domain.comment.dto.create.response.CommentCreateResponse;
import ject.componote.domain.comment.dto.find.response.CommentFindByComponentResponse;
import ject.componote.domain.comment.dto.find.response.CommentFindByMemberResponse;
import ject.componote.domain.comment.dto.find.response.CommentFindByParentResponse;
import ject.componote.domain.comment.dto.update.request.CommentUpdateRequest;
import ject.componote.domain.common.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private static final int DEFAULT_MEMBER_COMMENT_PAGE_SIZE = 8;
    private static final int DEFAULT_COMPONENT_COMMENT_PAGE_SIZE = 5;
    private static final int DEFAULT_REPLY_PAGE_SIZE = 5;

    private final CommentService commentService;

    @PostMapping("/comments")
    @User
    public ResponseEntity<?> create(
            @Authenticated final AuthPrincipal authPrincipal,
            @RequestBody @Valid final CommentCreateRequest commentCreateRequest
    ) {
        final CommentCreateResponse commentCreateResponse = commentService.create(authPrincipal, commentCreateRequest);
        return ResponseEntity.ok(commentCreateResponse);
    }

    @GetMapping("/members/comments")
    @User
    public ResponseEntity<PageResponse<CommentFindByMemberResponse>> getCommentsByMemberId(
            @Authenticated final AuthPrincipal authPrincipal,
            @PageableDefault(size = DEFAULT_MEMBER_COMMENT_PAGE_SIZE) final Pageable pageable
    ) {
        final PageResponse<CommentFindByMemberResponse> pageResponse = commentService.getCommentsByMemberId(authPrincipal, pageable);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/components/{componentId}/comments")
    public ResponseEntity<PageResponse<CommentFindByComponentResponse>> getCommentsByComponentId(
            @Authenticated final AuthPrincipal authPrincipal,
            @PathVariable("componentId") final Long componentId,
            @PageableDefault(size = DEFAULT_COMPONENT_COMMENT_PAGE_SIZE) final Pageable pageable
    ) {
        final PageResponse<CommentFindByComponentResponse> pageResponse = commentService.getCommentsByComponentId(authPrincipal, componentId, pageable);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/comments/{parentId}/replies")
    public ResponseEntity<PageResponse<CommentFindByParentResponse>> getRepliesByCommentId(
            @Authenticated final AuthPrincipal authPrincipal,
            @PathVariable("parentId") final Long parentId,
            @PageableDefault(size = DEFAULT_REPLY_PAGE_SIZE) final Pageable pageable
    ) {
        final PageResponse<CommentFindByParentResponse> pageResponse = commentService.getRepliesByComponentId(authPrincipal, parentId, pageable);
        return ResponseEntity.ok(pageResponse);
    }

    @PutMapping("/comments/{commentId}")
    @User
    public ResponseEntity<Void> update(@Authenticated final AuthPrincipal authPrincipal,
                                       @PathVariable("commentId") final Long commentId,
                                       @RequestBody @Valid final CommentUpdateRequest commentUpdateRequest) {
        commentService.update(authPrincipal, commentId, commentUpdateRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/comments/{commentId}/likes")
    @User
    public ResponseEntity<Void> likeComment(@Authenticated final AuthPrincipal authPrincipal,
                                            @PathVariable("commentId") final Long commentId) {
        commentService.likeComment(authPrincipal, commentId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/comments/{commentId}/likes")
    @User
    public ResponseEntity<Void> unlikeComment(@Authenticated final AuthPrincipal authPrincipal,
                                              @PathVariable("commentId") final Long commentId) {
        commentService.unlikeComment(authPrincipal, commentId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/comments/{commentId}")
    @User
    public ResponseEntity<Void> delete(@Authenticated final AuthPrincipal authPrincipal,
                                       @PathVariable("commentId") final Long commentId) {
        commentService.delete(authPrincipal, commentId);
        return ResponseEntity.noContent()
                .build();
    }
}
