package ject.componote.domain.design.dto.search.response;

import ject.componote.domain.design.domain.DesignSystem;

public record DesignSystemSearchResponse(
        String name,
        String organizationName,
        String url
) {
    public static DesignSystemSearchResponse from(DesignSystem designSystem) {
        String websiteUrl = designSystem.getLinks().getLinks().stream()
                .filter(link -> "WEBSITE".equalsIgnoreCase(link.getType().name()))
                .map(link -> link.getUrl().getValue())
                .findFirst()
                .orElse(null);

        return new DesignSystemSearchResponse(
                designSystem.getDesign().getSummary().getName(),
                designSystem.getDesign().getSummary().getOrganization(),
                websiteUrl
        );
    }
}
