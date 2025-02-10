package ject.componote.domain.design.dto.search.response;

import ject.componote.domain.design.domain.DesignSystem;

import java.util.List;
import java.util.stream.Collectors;

public record DesignSystemSearchResponse(
    String name,
    String organizationName,
    String description,
    List<DesignFilterSearchResponse> filters,
    List<DesignLinkResponse> links,
    Long recommendCount
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
                    .filter(link -> link.getUrl() != null)
                    .map(link -> new DesignLinkResponse(
                            link.getType().name().toLowerCase(),
                            link.getUrl().getValue()
                    ))
                    .collect(Collectors.toList()),
            designSystem.getDesign().getSummary().getRecommendCount()
    );
  }

}