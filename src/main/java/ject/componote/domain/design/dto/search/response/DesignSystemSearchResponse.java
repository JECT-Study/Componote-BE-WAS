package ject.componote.domain.design.dto.search.response;

import ject.componote.domain.design.domain.DesignSystem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record DesignSystemSearchResponse(
        String name,
        String organizationName,
        String description,
        List<DesignFilterSearchResponse> filters,
        Map<String, String> links
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
                        .collect(Collectors.toMap(
                                link -> link.getType().name().toLowerCase(),
                                link -> link.getUrl().getValue()
                        ))
        );
    }
}
