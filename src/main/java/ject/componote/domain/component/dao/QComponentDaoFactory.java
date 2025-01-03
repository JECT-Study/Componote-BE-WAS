package ject.componote.domain.component.dao;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.stereotype.Component;

import static ject.componote.domain.bookmark.domain.QBookmark.bookmark;
import static ject.componote.domain.component.domain.QComponent.component;

@Component
public class QComponentDaoFactory {
    public ConstructorExpression<ComponentSummaryDao> createForSummary(final boolean withBookmark) {
        if (withBookmark) {
            return Projections.constructor(
                    ComponentSummaryDao.class,
                    component.id,
                    component.summary,
                    component.type,
                    component.bookmarkCount,
                    component.commentCount,
                    component.designReferenceCount,
                    component.viewCount,
                    bookmark.isNotNull()
            );
        }

        return Projections.constructor(
                ComponentSummaryDao.class,
                component.id,
                component.summary,
                component.type,
                component.bookmarkCount,
                component.commentCount,
                component.designReferenceCount,
                component.viewCount,
                Expressions.asBoolean(false)
        );
    }
}
