package ject.componote.domain.comment.validation;

import ject.componote.domain.member.model.AuthPrincipal;
import ject.componote.domain.comment.dao.CommentRepository;
import ject.componote.domain.comment.error.NotFoundCommentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommenterValidationAspect {
    private final CommentRepository commentRepository;

    @Before(value = "@annotation(ject.componote.domain.comment.validation.CommenterValidation) && args(authPrincipal, commentId, ..)", argNames = "authPrincipal,commentId")
    public void validate(final AuthPrincipal authPrincipal, final Long commentId) {
        final Long memberId = authPrincipal.id();
        if (!commentRepository.existsByIdAndMemberId(commentId, memberId)) {
            throw new NotFoundCommentException(commentId, memberId);
        }
    }
}
