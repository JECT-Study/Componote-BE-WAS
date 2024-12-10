package ject.componote.domain.design.domain;

import ject.componote.domain.design.domain.filter.DesignFilter;
import ject.componote.domain.design.domain.filter.DesignFilters;
import ject.componote.domain.design.domain.link.DesignLink;
import ject.componote.domain.design.domain.link.DesignLinks;
import lombok.Getter;

import java.util.List;

@Getter
public class DesignSystem {
    private final Design design;
    private final DesignLinks links;
    private final DesignFilters filters;

    public DesignSystem(final Design design, final DesignLinks links, final DesignFilters filters) {
        // filters의 모든 designId가 design.getId()와 같은지를 검증
        this.design = design;
        this.links = links;
        this.filters = filters;
    }

    public static DesignSystem of(final Design design, final List<DesignLink> links, final List<DesignFilter> filters) {
        return new DesignSystem(design, new DesignLinks(links), new DesignFilters(filters));
    }
}
