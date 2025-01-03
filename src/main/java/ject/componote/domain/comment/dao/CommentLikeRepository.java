package ject.componote.domain.comment.dao;

import ject.componote.domain.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByCommentIdAndMemberId(final Long commentId, final Long memberId);
    void deleteByCommentIdAndMemberId(final Long commentId, final Long memberId);
}
