package ject.componote.domain.bookmark.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static ject.componote.domain.bookmark.domain.QBookmark.bookmark;
import static ject.componote.global.util.RepositoryUtils.eqExpression;

@RequiredArgsConstructor
public class BookmarkQueryDslImpl implements BookmarkQueryDsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByComponentIdAndMemberId(final Long componentId, final Long memberId) {
        final Integer fetchFirst = queryFactory.selectOne()
                .from(bookmark)
                .where(
                        eqExpression(bookmark.type, "component")
                                .and(eqExpression(bookmark.resourceId, componentId))
                                .and(eqExpression(bookmark.memberId, memberId))
                )
                .fetchFirst();

        return fetchFirst != null;
    }
}
