package ject.componote.domain.comment.dao;

import ject.componote.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQueryDsl {
    Optional<Comment> findByIdAndMemberId(final Long id, final Long memberId);
    boolean existsByIdAndMemberId(final Long id, final Long memberId);
    void deleteByIdAndMemberId(final Long commentId, final Long memberId);
}
