package ject.componote.domain.design.dto.search.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record DesignFilterSearchResponse(
        @JsonInclude(JsonInclude.Include.NON_NULL) String type,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<String> values) {
}
