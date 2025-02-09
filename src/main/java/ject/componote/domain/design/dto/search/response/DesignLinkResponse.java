package ject.componote.domain.design.dto.search.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DesignLinkResponse(
    String type,
    String url
) {
}