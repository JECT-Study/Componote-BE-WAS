package ject.componote.domain.comment.application.create;

import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;
import ject.componote.domain.comment.error.InvalidCommentCreateStrategyException;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Predicate;

@RequiredArgsConstructor
public enum CommentCreationStrategy {
    GENERAL_WITHOUT_IMAGE(
            request -> request.parentId() == null && request.imageTempKey() == null,
            (request, memberId, permanentObjectKey) ->
                    Comment.createWithoutImage(request.componentId(), memberId, request.content())
    ),
    GENERAL_WITH_IMAGE(
            request -> request.parentId() == null && request.imageTempKey() != null,
            (request, memberId, permanentObjectKey) ->
                    Comment.createWithImage(request.componentId(), memberId, request.content(), permanentObjectKey)
    ),
    REPLY_WITHOUT_IMAGE(
            request -> request.parentId() != null && request.imageTempKey() == null,
            (request, memberId, permanentObjectKey) ->
                    Comment.createReplyWithoutImage(request.componentId(), memberId, request.parentId(), request.content())
    ),
    REPLY_WITH_IMAGE(
            request -> request.parentId() != null && request.imageTempKey() != null,
            (request, memberId, permanentObjectKey) ->
                    Comment.createReplyWithImage(request.componentId(), memberId, request.parentId(), request.content(), permanentObjectKey)
    );

    private final Predicate<CommentCreateRequest> condition;
    private final CommentCreationFunction creationFunction;

    public static CommentCreationStrategy findByRequest(final CommentCreateRequest request) {
        return Arrays.stream(values())
                .filter(type -> type.condition.test(request))
                .findFirst()
                .orElseThrow(InvalidCommentCreateStrategyException::new);
    }

    public Comment createComment(final CommentCreateRequest request, final Long memberId, final String permanentObjectKey) {
        return creationFunction.create(request, memberId, permanentObjectKey);
    }
}
