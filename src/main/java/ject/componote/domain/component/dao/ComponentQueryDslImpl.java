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
    public Page<ComponentSummaryDao> findAllByKeywordAndTypes(final String keyword, final List<ComponentType> types, final Pageable pageable) {
        return findAllWithConditions(null, keyword, types, pageable);
    }

    @Override
    public Page<ComponentSummaryDao> findAllByKeywordAndTypesWithBookmark(final Long memberId, final String keyword, final List<ComponentType> types, final Pageable pageable) {
        return findAllWithConditions(memberId, keyword, types, pageable);
    }

    private Page<ComponentSummaryDao> findAllWithConditions(final Long memberId,
                                                            final String keyword,
                                                            final List<ComponentType> types,
                                                            final Pageable pageable) {
        final JPAQuery<Long> countQuery = createCountQuery(memberId, keyword, types);
        final JPAQuery<ComponentSummaryDao> contentQuery = createContentQuery(memberId, keyword, types);
        return toPage(contentQuery, countQuery, component, pageable);
    }

    private JPAQuery<Long> createCountQuery(final Long memberId, final String keyword, final List<ComponentType> types) {
        return createBaseQuery(memberId, keyword, types)
                .select(component.countDistinct());
    }

    private JPAQuery<ComponentSummaryDao> createContentQuery(final Long memberId, final String keyword, final List<ComponentType> types) {
        return createBaseQuery(memberId, keyword, types)
                .select(componentDaoFactory.createForSummary(memberId))
                .distinct();    // 중복 데이터 임시 해결
    }

    private JPAQuery<?> createBaseQuery(final Long memberId, final String keyword, final List<ComponentType> types) {
        final JPAQuery<?> query = queryFactory
                .select(component)
                .from(component)
                .leftJoin(component.mixedNames, mixedName);

        if (memberId != null) {
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
