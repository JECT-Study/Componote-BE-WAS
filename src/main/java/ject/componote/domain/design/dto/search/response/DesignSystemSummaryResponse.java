package ject.componote.domain.design.dto.search.response;

import ject.componote.domain.design.domain.DesignSystem;

import java.util.List;
import java.util.stream.Collectors;

public record DesignSystemSummaryResponse(
        String name,
        String organizationName,
        String description,
        List<DesignFilterSearchResponse> filters,
        List<DesignLinkResponse> links,
        Long recommendCount,
        String thumbnailUrl,
        Long bookmarkCount,
        Boolean isBookmarked
) {
    public static DesignSystemSummaryResponse from(final DesignSystem designSystem, final Boolean isBookmarked) {
        return new DesignSystemSummaryResponse(
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
                designSystem.getDesign().getSummary().getRecommendCount().getValue(),
                designSystem.getDesign().getSummary().getThumbnail().toUrl(),
                designSystem.getDesign().getBookmarkCount().getValue(),
                isBookmarked
        );
    }
}

