package ject.componote.domain.design.dto.search.response;

import java.util.List;

public record DesignSystemSearchResponse(String name, String organizationName, String description, List<DesignFilterSearchResponse> filters) {
}
