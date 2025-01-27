package ject.componote.domain.comment.dao;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import ject.componote.domain.comment.domain.QComment;
import org.springframework.stereotype.Component;

import static ject.componote.domain.comment.domain.QComment.comment;
import static ject.componote.domain.comment.domain.QCommentLike.commentLike;
import static ject.componote.domain.component.domain.QComponent.component;
import static ject.componote.domain.member.domain.QMember.member;

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
                comment.replyCount,
                Expressions.asBoolean(false),
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
                comment.image,
                comment.content,
                comment.createdAt,
                comment.likeCount,
                comment.replyCount,
                commentLike.isNotNull(),
                comment.parentId.isNotNull()
        );
    }

    public ConstructorExpression<CommentFindByParentDao> createForParent() {
        return Projections.constructor(
                CommentFindByParentDao.class,
                member.id,
                member.nickname,
                member.profileImage,
                member.job,
                comment.id,
                comment.image,
                comment.content,
                comment.createdAt,
                comment.likeCount,
                Expressions.asBoolean(false)
        );
    }

    public ConstructorExpression<CommentFindByParentDao> createForParentWithLikeStatus() {
        return Projections.constructor(
                CommentFindByParentDao.class,
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
                commentLike.isNotNull()
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
