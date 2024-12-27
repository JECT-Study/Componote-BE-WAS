package ject.componote.domain.comment.dao;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import ject.componote.domain.comment.domain.QComment;
import org.springframework.stereotype.Component;

import static ject.componote.domain.auth.domain.QMember.member;
import static ject.componote.domain.comment.domain.QComment.comment;
import static ject.componote.domain.comment.domain.QCommentLike.commentLike;
import static ject.componote.domain.component.domain.QComponent.component;

@Component
public class QCommentDaoFactory {
    public ConstructorExpression<CommentFindByComponentDao> createForComponent() {
        return Projections.constructor(
                CommentFindByComponentDao.class,
                member.id,
                member.nickname,
                member.profileImage,
                member.job,
                comment.id,
                comment.parentId,
                comment.image,
                comment.content,
                comment.createdAt,
                comment.likeCount,
                null,
                comment.parentId.isNotNull()
        );
    }

    public ConstructorExpression<CommentFindByComponentDao> createForComponentWithLikeStatus() {
        return Projections.constructor(
                CommentFindByComponentDao.class,
                member.id,
                member.nickname,
                member.profileImage,
                member.job,
                comment.id,
                comment.parentId,
                comment.image,
                comment.content,
                comment.createdAt,
                comment.likeCount,
                commentLike.isNotNull(),
                comment.parentId.isNotNull()
        );
    }

    public ConstructorExpression<CommentFindByMemberDao> createForMember(final QComment parent) {
        return Projections.constructor(
                CommentFindByMemberDao.class,
                comment.id,
                comment.parentId,
                component.summary.title,
                parent.content,
                comment.content,
                comment.createdAt,
                comment.parentId.isNotNull()
        );
    }
}
