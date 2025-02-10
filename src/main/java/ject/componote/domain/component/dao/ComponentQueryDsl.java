package ject.componote.domain.component.dao;

import ject.componote.domain.component.domain.Component;
import ject.componote.domain.component.domain.ComponentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ComponentQueryDsl {
    Set<Long> findAllComponentIdsByKeyword(final String keyword, final Pageable pageable);
    Set<Long> findAllComponentIdsByTypes(final List<ComponentType> types, final Pageable pageable);
    Page<Component> findAllByComponentIdsAndKeyword(final Set<Long> componentIds, final String keyword, final Pageable pageable);
    Page<Component> findAllByComponentIdsAndTypes(final Set<Long> componentIds, final List<ComponentType> types, final Pageable pageable);
}
