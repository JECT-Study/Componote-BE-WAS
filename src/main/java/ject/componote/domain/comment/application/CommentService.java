package ject.componote.domain.comment.application;

import ject.componote.domain.auth.model.AuthPrincipal;
import ject.componote.domain.comment.dao.CommentFindByComponentDao;
import ject.componote.domain.comment.dao.CommentLikeRepository;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.domain.Comment;
import ject.componote.domain.comment.dto.create.request.CommentCreateRequest;
import ject.componote.domain.comment.dto.create.response.CommentCreateResponse;
import ject.componote.domain.comment.dto.find.response.CommentFindByComponentResponse;
import ject.componote.domain.comment.dto.find.response.CommentFindByMemberResponse;
import ject.componote.domain.comment.dto.like.event.CommentLikeEvent;
import ject.componote.domain.comment.dto.like.event.CommentUnlikeEvent;
import ject.componote.domain.comment.dto.update.request.CommentUpdateRequest;
import ject.componote.domain.comment.error.AlreadyLikedException;
import ject.componote.domain.comment.error.NoLikedException;
import ject.componote.domain.comment.error.NotFoundCommentException;
import ject.componote.domain.comment.error.NotFoundParentCommentException;
import ject.componote.domain.comment.model.CommentContent;
import ject.componote.domain.comment.model.CommentImage;
import ject.componote.domain.comment.validation.CommenterValidation;
import ject.componote.domain.common.dto.response.PageResponse;
import ject.componote.infra.file.application.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {
    private final ApplicationEventPublisher eventPublisher;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final FileService fileService;

    @Transactional
    public CommentCreateResponse create(final AuthPrincipal authPrincipal, final CommentCreateRequest request) {
        if (isReply(request)) {
            validateParentId(request.parentId());
        }

        final Comment comment = commentRepository.save(
                CommentCreationStrategy.createBy(request, authPrincipal.id())
        );
        fileService.moveImage(comment.getImage().getImage());
        return CommentCreateResponse.from(comment);
    }

    public PageResponse<CommentFindByMemberResponse> getCommentsByMemberId(final AuthPrincipal authPrincipal,
                                                                           final Pageable pageable) {
        final Page<CommentFindByMemberResponse> page = commentRepository.findAllByMemberIdWithPagination(authPrincipal.id(), pageable)
                .map(CommentFindByMemberResponse::from);
        return PageResponse.from(page);
    }

    public PageResponse<CommentFindByComponentResponse> getCommentsByComponentId(final AuthPrincipal authPrincipal,
                                                                                 final Long componentId,
                                                                                 final Pageable pageable) {
        final Page<CommentFindByComponentResponse> page = findCommentsByComponentId(authPrincipal, componentId, pageable)
                .map(CommentFindByComponentResponse::from);
        return PageResponse.from(page);
    }

    @CommenterValidation
    @Transactional
    public void update(final AuthPrincipal authPrincipal, final Long commentId, final CommentUpdateRequest commentUpdateRequest) {
        final Comment comment = findCommentByIdAndMemberId(commentId, authPrincipal.id());

        final CommentImage image = CommentImage.from(commentUpdateRequest.imageObjectKey());
        if (!comment.equalsImage(image)) {
            fileService.moveImage(image.getImage());
        }

        final CommentContent content = CommentContent.from(commentUpdateRequest.content()); // 가비지가 발생하지 않을까?
        comment.update(content, image);

        commentRepository.save(comment);
    }

    @CommenterValidation
    @Transactional
    public void delete(final AuthPrincipal authPrincipal, final Long commentId) {
        commentRepository.deleteByIdAndMemberId(commentId, authPrincipal.id());
    }

    public void likeComment(final AuthPrincipal authPrincipal, final Long commentId) {
        final Long memberId = authPrincipal.id();
        if (isAlreadyLiked(commentId, memberId)) {
            throw new AlreadyLikedException(commentId, memberId);
        }

        eventPublisher.publishEvent(CommentLikeEvent.of(authPrincipal, commentId));
    }

    public void unlikeComment(final AuthPrincipal authPrincipal, final Long commentId) {
        final Long memberId = authPrincipal.id();
        if (!isAlreadyLiked(commentId, memberId)) {
            throw new NoLikedException(commentId, memberId);
        }

        eventPublisher.publishEvent(CommentUnlikeEvent.of(authPrincipal, commentId));
    }

    private Comment findCommentByIdAndMemberId(final Long commentId, final Long memberId) {
        return commentRepository.findByIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new NotFoundCommentException(commentId, memberId));
    }

    private Page<CommentFindByComponentDao> findCommentsByComponentId(final AuthPrincipal authPrincipal,
                                                                      final Long componentId,
                                                                      final Pageable pageable) {
        if (authPrincipal == null) {
            return commentRepository.findAllByComponentIdWithPagination(componentId, pageable);
        }

        final Long memberId = authPrincipal.id();
        return commentRepository.findAllByComponentIdWithLikeStatusAndPagination(componentId, memberId, pageable);
    }

    private boolean isReply(final CommentCreateRequest request) {
        return request.parentId() != null;
    }

    private void validateParentId(final Long parentId) {
        if (!commentRepository.existsById(parentId)) {
            throw new NotFoundParentCommentException(parentId);
        }
    }

    private boolean isAlreadyLiked(final Long commentId, final Long memberId) {
        return commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId);
    }
}
