package ject.componote.domain.component.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ject.componote.domain.component.domain.ComponentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ject.componote.domain.bookmark.domain.QBookmark.bookmark;
import static ject.componote.domain.component.domain.QComponent.component;
import static ject.componote.domain.component.domain.QMixedName.mixedName;
import static ject.componote.global.util.RepositoryUtils.createOrderSpecifiers;
import static ject.componote.global.util.RepositoryUtils.eqExpression;

@Slf4j
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
        final Set<Long> componentIds = findComponentIdsWithCondition(memberId, keyword, types, pageable);    // 페이징이 적용된 상태에서 중복 제거 X
        final JPAQuery<ComponentSummaryDao> contentQuery = createContentQuery(memberId, componentIds, pageable);
        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }

    private Set<Long> findComponentIdsWithCondition(final Long memberId, final String keyword, final List<ComponentType> types, final Pageable pageable) {
        final List<Long> componentIds = createBaseQuery(memberId, keyword, types)
                .select(component.id)
                .groupBy(component.id)  // SELECT 절에 없는 컬럼을 가지고 ORDER BY를 수행하므로 GROUP BY 사용
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(createOrderSpecifiers(component, pageable))
                .orderBy(component.id.asc())    // fileSort 가 unstable 하여 Unique 정렬 조건 추가
                .fetch();
        return new HashSet<>(componentIds);
    }

    private JPAQuery<Long> createCountQuery(final Long memberId, final String keyword, final List<ComponentType> types) {
        return createBaseQuery(memberId, keyword, types)
                .select(component.countDistinct());
    }

    private JPAQuery<ComponentSummaryDao> createContentQuery(final Long memberId, final Set<Long> componentIds, final Pageable pageable) {
        final JPAQuery<ComponentSummaryDao> contentQuery = queryFactory.select(componentDaoFactory.createForSummary(memberId))
                .from(component)
                .where(component.id.in(componentIds))
                .orderBy(createOrderSpecifiers(component, pageable))
                .orderBy(component.id.asc());   // fileSort 가 stable 하지 않아 Unique 정렬 조건 추가
        return applyBookmarkJoin(contentQuery, memberId);
    }

    private JPAQuery<?> createBaseQuery(final Long memberId, final String keyword, final List<ComponentType> types) {
        final JPAQuery<?> query = queryFactory.from(component)
                .leftJoin(component.mixedNames, mixedName)
                .where(createSearchCondition(keyword, types));
        return applyBookmarkJoin(query, memberId);
    }

    private <T> JPAQuery<T> applyBookmarkJoin(final JPAQuery<T> query, final Long memberId) {
        if (memberId != null) {
            query.innerJoin(bookmark)
                    .on(eqExpression(bookmark.resourceId, component.id));
        }
        return query;
    }

    private BooleanExpression createSearchCondition(final String keyword, final List<ComponentType> types) {
        final BooleanExpression keywordCondition = createKeywordCondition(keyword);
        if (types != null && !types.isEmpty()) {
            return component.type.in(types).and(keywordCondition);
        }
        return keywordCondition;
    }

    private BooleanExpression createKeywordCondition(final String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return mixedName.name.contains(keyword)
                .or(component.summary.title.contains(keyword));
    }
}
