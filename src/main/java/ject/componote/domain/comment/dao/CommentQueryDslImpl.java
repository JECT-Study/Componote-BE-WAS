package ject.componote.domain.comment.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ject.componote.domain.comment.domain.QComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static ject.componote.domain.auth.domain.QMember.member;
import static ject.componote.domain.comment.domain.QComment.comment;
import static ject.componote.domain.comment.domain.QCommentLike.commentLike;
import static ject.componote.domain.component.domain.QComponent.component;
import static ject.componote.global.util.RepositoryUtils.eqExpression;
import static ject.componote.global.util.RepositoryUtils.toPage;

@RequiredArgsConstructor
public class CommentQueryDslImpl implements CommentQueryDsl {
    private static final QComment PARENT = new QComment("parent");

    private final JPAQueryFactory queryFactory;
    private final QCommentDaoFactory qCommentDaoFactory;

    @Override
    public Page<CommentFindByComponentDao> findAllByComponentIdWithPagination(final Long componentId, final Pageable pageable) {
        final BooleanExpression predicate = eqExpression(comment.componentId, componentId);
        final JPAQuery<Long> countQuery = createCountQuery(predicate);
        final JPAQuery<CommentFindByComponentDao> baseQuery = queryFactory.select(qCommentDaoFactory.createForComponent())
                .from(comment)
                .innerJoin(member).on(eqExpression(member.id, comment.memberId))
                .where(predicate);
        return toPage(baseQuery, countQuery, comment, pageable);
    }

    @Override
    public Page<CommentFindByComponentDao> findAllByComponentIdWithLikeStatusAndPagination(final Long componentId, final Long memberId, final Pageable pageable) {
        final BooleanExpression predicate = eqExpression(comment.componentId, componentId);
        final JPAQuery<Long> countQuery = createCountQuery(predicate);
        final JPAQuery<CommentFindByComponentDao> baseQuery = queryFactory.select(qCommentDaoFactory.createForComponentWithLikeStatus())
                .from(comment)
                .innerJoin(member).on(eqExpression(member.id, comment.memberId))
                .innerJoin(commentLike).on(eqExpression(commentLike.commentId, component.id).and(eqExpression(commentLike.memberId, memberId)))
                .where(predicate);
        return toPage(baseQuery, countQuery, comment, pageable);
    }

    @Override
    public Page<CommentFindByParentDao> findAllByParentIdWithPagination(final Long parentId, final Pageable pageable) {
        final BooleanExpression predicate = eqExpression(comment.parentId, parentId);
        final JPAQuery<Long> countQuery = createCountQuery(predicate);
        final JPAQuery<CommentFindByParentDao> baseQuery = queryFactory.select(qCommentDaoFactory.createForParent())
                .from(comment)
                .innerJoin(member).on(eqExpression(member.id, comment.memberId))
                .where(predicate);
        return toPage(baseQuery, countQuery, comment, pageable);
    }

    @Override
    public Page<CommentFindByParentDao> findAllByParentIdWithLikeStatusAndPagination(final Long parentId, final Long memberId, final Pageable pageable) {
        final BooleanExpression predicate = eqExpression(comment.parentId, parentId);
        final JPAQuery<Long> countQuery = createCountQuery(predicate);
        final JPAQuery<CommentFindByParentDao> baseQuery = queryFactory.select(qCommentDaoFactory.createForParent())
                .from(comment)
                .innerJoin(member).on(eqExpression(member.id, comment.memberId))
                .innerJoin(commentLike).on(eqExpression(commentLike.commentId, component.id).and(eqExpression(commentLike.memberId, memberId)))
                .where(predicate);
        return toPage(baseQuery, countQuery, comment, pageable);
    }

    @Override
    public Page<CommentFindByMemberDao> findAllByMemberIdWithPagination(final Long memberId, final Pageable pageable) {
        final BooleanExpression predicate = eqExpression(comment.memberId, memberId);
        final JPAQuery<Long> countQuery = createCountQuery(predicate);
        final JPAQuery<CommentFindByMemberDao> baseQuery = queryFactory.select(qCommentDaoFactory.createForMember(PARENT))
                .from(comment)
                .innerJoin(PARENT).on(eqExpression(PARENT.id, comment.parentId))
                .innerJoin(component).on(eqExpression(component.id, comment.componentId))
                .where(predicate);
        return toPage(baseQuery, countQuery, comment, pageable);
    }

    private JPAQuery<Long> createCountQuery(final BooleanExpression predicate) {
        return queryFactory.select(comment.count())
                .from(comment)
                .where(predicate);
    }
}
