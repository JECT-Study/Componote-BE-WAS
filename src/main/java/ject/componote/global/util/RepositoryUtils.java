package ject.componote.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

public final class RepositoryUtils {
    private RepositoryUtils() {

    }

    public static <T, E> Page<T> toPage(final JPAQuery<T> baseQuery,
                                        final JPAQuery<Long> countQuery,
                                        final EntityPathBase<E> qClass,
                                        final Pageable pageable) {
        if (countQuery.fetchFirst() == null) {
            return Page.empty();
        }

        final JPAQuery<T> contentQuery = createContentQuery(baseQuery, qClass, pageable);
        return PageableExecutionUtils.getPage(contentQuery.fetch(), pageable, countQuery::fetchOne);
    }

    public static <T extends ComparableExpression<?>> BooleanExpression eqExpression(final SimpleExpression<T> simpleExpression, final T target) {
        if (target == null) {
            return null;
        }

        return simpleExpression.eq(target);
    }

    public static <T extends Number & Comparable<?>> BooleanExpression eqExpression(final NumberExpression<T> numberExpression, final T target) {
        if (target == null) {
            return null;
        }

        return numberExpression.eq(target);
    }

    public static BooleanExpression eqExpression(final StringExpression numberExpression, final String target) {
        if (target == null) {
            return null;
        }

        return numberExpression.eq(target);
    }

    public static <T extends Comparable<?>> BooleanExpression eqExpression(final SimpleExpression<T> simpleExpression, final SimpleExpression<T> target) {
        return simpleExpression.eq(target);
    }

    private static <T, E> JPAQuery<T> createContentQuery(final JPAQuery<T> query,
                                                         final EntityPathBase<E> qClass,
                                                         final Pageable pageable) {
        return query.limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(createOrderSpecifiers(qClass, pageable));
    }

    public static <E> OrderSpecifier<?>[] createOrderSpecifiers(final EntityPathBase<E> qClass, final Pageable pageable) {
        return pageable.getSort()
                .stream()
                .map(sort -> toOrderSpecifier(qClass, sort))
                .toArray(OrderSpecifier[]::new);
    }

    private static <E> OrderSpecifier<?> toOrderSpecifier(final EntityPathBase<E> qClass, final Sort.Order sortOrder) {
        final Order orderMethod = toOrder(sortOrder);
        final PathBuilder<? extends E> pathBuilder = new PathBuilder<>(qClass.getType(), qClass.getMetadata());
        return new OrderSpecifier(orderMethod, pathBuilder.get(sortOrder.getProperty()));
    }

    private static Order toOrder(final Sort.Order sortOrder) {
        if (sortOrder.isAscending()) {
            return Order.ASC;
        }

        return Order.DESC;
    }
}
