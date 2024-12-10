package ject.componote.domain.design.dto.search.request;

import ject.componote.domain.design.domain.filter.FilterType;

import java.util.List;

public record DesignFilterSearchRequest(String type, List<String> values) {
    public FilterType parseType() {
        return FilterType.valueOf(type);
    }
}
