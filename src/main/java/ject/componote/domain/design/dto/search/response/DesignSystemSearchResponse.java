package ject.componote.domain.design.dto.search.response;

import ject.componote.domain.design.domain.DesignSystem;

import java.util.List;
import java.util.stream.Collectors;

public record DesignSystemSearchResponse(
    String name,
    String organizationName,
    String description,
    List<DesignFilterSearchResponse> filters,
    List<DesignLinkResponse> links
) {
  public static DesignSystemSearchResponse from(DesignSystem designSystem) {
    return new DesignSystemSearchResponse(
            designSystem.getDesign().getSummary().getName(),
            designSystem.getDesign().getSummary().getOrganization(),
            designSystem.getDesign().getSummary().getDescription(),
            designSystem.getFilters().getFilters().stream()
                    .map(filter -> new DesignFilterSearchResponse(filter.getType().name(), List.of(filter.getValue())))
                    .collect(Collectors.toList()),
            designSystem.getLinks().getLinks().stream()
                    .filter(link -> link.getUrl() != null) // ✅ URL이 null이 아닌 경우만 처리
                    .map(link -> new DesignLinkResponse(
                            link.getType().name().toLowerCase(),
                            link.getUrl().getValue() // ✅ null이면 빈 문자열 처리
                    ))
                    .collect(Collectors.toList())
    );
  }

}