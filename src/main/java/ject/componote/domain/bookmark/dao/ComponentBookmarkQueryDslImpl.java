package ject.componote.domain.bookmark.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static ject.componote.domain.bookmark.domain.QComponentBookmark.componentBookmark;

@RequiredArgsConstructor
public class ComponentBookmarkQueryDslImpl implements ComponentBookmarkQueryDsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public Set<Long> findAllComponentIdsByMemberIdAndComponentIds(final Long memberId, final Set<Long> componentIds) {
        if (memberId == null || componentIds.isEmpty()) {
            return Collections.emptySet();
        }
        return new HashSet<>(
                queryFactory.select(componentBookmark.componentId)
                .from(componentBookmark)
                .where(componentBookmark.memberId.eq(memberId).and(componentBookmark.componentId.in(componentIds)))
                .fetch()
        );
    }
}
