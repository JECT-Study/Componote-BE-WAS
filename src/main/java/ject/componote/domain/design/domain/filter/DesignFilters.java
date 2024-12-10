package ject.componote.domain.design.domain.filter;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class DesignFilters {
    private final List<DesignFilter> filters;

    public DesignFilters(final List<DesignFilter> filters) {
        this.filters = filters;
    }

    public Map<FilterType, List<String>> getValuesByType() {
        return filters.stream()
                .collect(Collectors.groupingBy(
                        DesignFilter::getType,
                        Collectors.mapping(DesignFilter::getValue, Collectors.toList()))
                );
    }
}
