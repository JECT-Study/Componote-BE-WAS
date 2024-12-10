package ject.componote.domain.design.dto.search.request;

import java.util.List;

public record DesignSystemSearchRequest(String keyword, List<DesignFilterSearchRequest> filters) {
}
