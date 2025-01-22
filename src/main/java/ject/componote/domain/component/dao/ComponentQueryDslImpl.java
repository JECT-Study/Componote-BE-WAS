package ject.componote.domain.component.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ject.componote.domain.component.domain.ComponentType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static ject.componote.domain.bookmark.domain.QBookmark.bookmark;
import static ject.componote.domain.component.domain.QComponent.component;
import static ject.componote.domain.component.domain.QMixedName.mixedName;
import static ject.componote.global.util.RepositoryUtils.eqExpression;
import static ject.componote.global.util.RepositoryUtils.toPage;

@RequiredArgsConstructor
public class ComponentQueryDslImpl implements ComponentQueryDsl {
    private final QComponentDaoFactory componentDaoFactory;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ComponentSummaryDao> searchByKeyword(final String keyword, final Pageable pageable) {
        return search(null, keyword, null, pageable, false);
    }

    @Override
    public Page<ComponentSummaryDao> searchByKeywordWithTypes(final String keyword, final List<ComponentType> types, final Pageable pageable) {
        return search(null, keyword, types, pageable, false);
    }

    @Override
    public Page<ComponentSummaryDao> searchWithBookmark(final Long memberId, final String keyword, final Pageable pageable) {
        return search(memberId, keyword, null, pageable, true);
    }

    @Override
    public Page<ComponentSummaryDao> searchWithBookmarkAndTypes(final Long memberId, final String keyword, final List<ComponentType> types, final Pageable pageable) {
        return search(memberId, keyword, types, pageable, true);
    }

    public Page<ComponentSummaryDao> search(final Long memberId,
                                            final String keyword,
                                            final List<ComponentType> types,
                                            final Pageable pageable,
                                            final boolean withBookmark) {
        final JPAQuery<Long> countQuery = createCountQuery(keyword, types);
        final JPAQuery<ComponentSummaryDao> baseQuery = createBaseQuery(memberId, keyword, types, withBookmark);
        return toPage(baseQuery, countQuery, component, pageable);
    }

    private JPAQuery<Long> createCountQuery(final String keyword, final List<ComponentType> types) {
        return queryFactory.select(component.countDistinct())
                .from(component)
                .leftJoin(component.mixedNames, mixedName)
                .where(createSearchCondition(keyword, types));
    }

    private JPAQuery<ComponentSummaryDao> createBaseQuery(final Long memberId,
                                                          final String keyword,
                                                          final List<ComponentType> types,
                                                          final boolean withBookmark) {
        final JPAQuery<ComponentSummaryDao> query = queryFactory.selectDistinct(componentDaoFactory.createForSummary(withBookmark))
                .from(component)
                .leftJoin(component.mixedNames, mixedName);

        if (withBookmark && memberId != null) {
            query.leftJoin(bookmark)
                    .on(eqExpression(bookmark.resourceId, component.id)    // 임시 수정
                            .and(eqExpression(bookmark.memberId, memberId)));
        }

        return query.where(createSearchCondition(keyword, types));
    }

    private BooleanExpression createSearchCondition(final String keyword, final List<ComponentType> types) {
        final BooleanExpression keywordCondition = createKeywordCondition(keyword);
        if (types != null && !types.isEmpty()) {
            return component.type.in(types).and(keywordCondition);
        }

        return keywordCondition;
    }

    private static BooleanExpression createKeywordCondition(final String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return mixedName.name.contains(keyword)
                .or(component.summary.title.contains(keyword));
    }
}
