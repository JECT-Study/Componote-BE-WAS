package ject.componote.domain.comment.application;

import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;
import ject.componote.domain.comment.error.InvalidCommentCreateStrategyException;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Predicate;

@RequiredArgsConstructor
public enum CommentCreationStrategy {
    GENERAL_WITHOUT_IMAGE(
            request -> request.parentId() == null && request.imageObjectKey() == null,
            (request, memberId) ->
                    Comment.createWithoutImage(request.componentId(), memberId, request.content())
    ),
    GENERAL_WITH_IMAGE(
            request -> request.parentId() == null && request.imageObjectKey() != null,
            (request, memberId) ->
                    Comment.createWithImage(request.componentId(), memberId, request.content(), request.imageObjectKey())
    ),
    REPLY_WITHOUT_IMAGE(
            request -> request.parentId() != null && request.imageObjectKey() == null,
            (request, memberId) ->
                    Comment.createReplyWithoutImage(request.componentId(), memberId, request.parentId(), request.content())
    ),
    REPLY_WITH_IMAGE(
            request -> request.parentId() != null && request.imageObjectKey() != null,
            (request, memberId) ->
                    Comment.createReplyWithImage(request.componentId(), memberId, request.parentId(), request.content(), request.imageObjectKey())
    );

    private final Predicate<CommentCreateRequest> condition;
    private final CommentCreationFunction creationFunction;

    public static Comment createBy(final CommentCreateRequest request, final Long memberId) {
        return Arrays.stream(values())
                .filter(type -> type.condition.test(request))
                .findFirst()
                .orElseThrow(InvalidCommentCreateStrategyException::new)
                .creationFunction.create(request, memberId);
    }

    @FunctionalInterface
    private interface CommentCreationFunction {
        Comment create(final CommentCreateRequest request, final Long memberId);
    }
}
