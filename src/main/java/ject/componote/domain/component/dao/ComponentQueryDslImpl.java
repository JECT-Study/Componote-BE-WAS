package ject.componote.domain.component.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ject.componote.domain.component.domain.QComponent.component;
import static ject.componote.domain.component.domain.QMixedName.mixedName;
import static ject.componote.global.util.RepositoryUtils.createOrderSpecifiers;

@Slf4j
@RequiredArgsConstructor
public class ComponentQueryDslImpl implements ComponentQueryDsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public Set<Long> findAllComponentIdsByKeyword(final String keyword, final Pageable pageable) {
        return new HashSet<>(
                queryFactory.select(component.id)
                        .from(component)
                        .where(createKeywordCondition(keyword))
                        .leftJoin(component.mixedNames, mixedName)
                        .groupBy(component.id)  // SELECT 절에 없는 컬럼을 가지고 ORDER BY를 수행하므로 GROUP BY 사용
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .orderBy(createOrderSpecifiers(component, pageable))
                        .orderBy(component.id.asc())    // fileSort 가 unstable 하여 Unique 정렬 조건 추가
                        .fetch()
        );
    }

    @Override
    public Set<Long> findAllComponentIdsByTypes(final List<ComponentType> types, final Pageable pageable) {
        return new HashSet<>(
                queryFactory.select(component.id)
                        .from(component)
                        .where(createTypesCondition(types))
                        .groupBy(component.id)
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .orderBy(createOrderSpecifiers(component, pageable))
                        .orderBy(component.id.asc())
                        .fetch()
        );
    }

    @Override
    public Page<Component> findAllByComponentIdsAndKeyword(final Set<Long> componentIds,
                                                           final String keyword,
                                                           final Pageable pageable) {
        if (componentIds.isEmpty()) {
            return Page.empty(pageable);
        }
        final JPAQuery<Long> countQuery = queryFactory.select(component.countDistinct())
                .from(component)
                .leftJoin(component.mixedNames, mixedName)
                .where(createKeywordCondition(keyword));
        final JPAQuery<Component> contentQuery = queryFactory.selectFrom(component)
                .where(component.id.in(componentIds))
                .leftJoin(component.mixedNames, mixedName)
                .orderBy(createOrderSpecifiers(component, pageable))
                .orderBy(component.id.asc())
                .fetchJoin();
        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Component> findAllByComponentIdsAndTypes(final Set<Long> componentIds,
                                                         final List<ComponentType> types,
                                                         final Pageable pageable) {
        if (componentIds.isEmpty()) {
            return Page.empty(pageable);
        }
        final JPAQuery<Long> countQuery = queryFactory.select(component.count())
                .from(component)
                .where(createTypesCondition(types));
        final JPAQuery<Component> contentQuery = queryFactory.selectFrom(component)
                .where(component.id.in(componentIds))
                .orderBy(createOrderSpecifiers(component, pageable))
                .orderBy(component.id.asc());
        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }

    private BooleanExpression createTypesCondition(final List<ComponentType> types) {
        if (types == null || types.isEmpty()) {
            return null;
        }
        return component.type.in(types);
    }

    private BooleanExpression createKeywordCondition(final String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return mixedName.name.contains(keyword)
                .or(component.summary.title.contains(keyword));
    }
}
