package ject.componote.domain.comment.application;

import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;
import ject.componote.domain.comment.error.InvalidCommentCreateStrategyException;
import ject.componote.domain.comment.model.CommentImage;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Predicate;

@RequiredArgsConstructor
public enum CommentCreationStrategy {
    GENERAL_WITHOUT_IMAGE(
            request -> request.parentId() == null && request.imageObjectKey() == null,
            (request, memberId, image) ->
                    Comment.createWithoutImage(request.componentId(), memberId, request.content())
    ),
    GENERAL_WITH_IMAGE(
            request -> request.parentId() == null && request.imageObjectKey() != null,
            (request, memberId, image) ->
                    Comment.createWithImage(request.componentId(), memberId, request.content(), image)
    ),
    REPLY_WITHOUT_IMAGE(
            request -> request.parentId() != null && request.imageObjectKey() == null,
            (request, memberId, image) ->
                    Comment.createReplyWithoutImage(request.componentId(), memberId, request.parentId(), request.content())
    ),
    REPLY_WITH_IMAGE(
            request -> request.parentId() != null && request.imageObjectKey() != null,
            (request, memberId, image) ->
                    Comment.createReplyWithImage(request.componentId(), memberId, request.parentId(), request.content(), image)
    );

    private final Predicate<CommentCreateRequest> condition;
    private final CommentCreationFunction creationFunction;

    public static CommentCreationStrategy findByRequest(final CommentCreateRequest request) {
        return Arrays.stream(values())
                .filter(type -> type.condition.test(request))
                .findFirst()
                .orElseThrow(InvalidCommentCreateStrategyException::new);
    }

    public Comment createComment(final CommentCreateRequest request, final Long memberId, final CommentImage image) {
        return creationFunction.create(request, memberId, image);
    }

    @FunctionalInterface
    private interface CommentCreationFunction {
        Comment create(final CommentCreateRequest request, final Long memberId, final CommentImage image);
    }
}
