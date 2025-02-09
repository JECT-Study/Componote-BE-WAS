package ject.componote.domain.design.domain.filter;

import lombok.Getter;

import java.util.List;

@Getter
public enum FilterType {
    PLATFORM(List.of("GITHUB", "FIGMA", "STORYBOOK", "ZEROHEIGHT")),
    TECHNOLOGY(List.of("ANGULAR", "NONE", "CSS", "CSS_IN_JS", "CSS_MODULES", "HTML",
            "REACT", "SASS", "STIMULUS", "SVELTE", "TAILWIND_CSS", "TWIG", "VANILLA_JS", "VUE",
            "WEB_COMPONENTS")),
    /*
    DESIGN_TOKEN: 디자인 토큰, ICON: 아이콘, OPENSOURCE: 오픈소스, EXAMPLE: 용례,
    BRAND_PRINCIPLES: 브랜드 원칙, ACCESSIBILITY_INFORMATION: 접근성 안내,
    VOICE_AND_TONE: 보이스와 톤, CODE_EXAMPLE: 코드 예제
    */
    CONTENT(List.of("DESIGN_TOKEN", "ICON", "OPENSOURCE", "EXAMPLE", "BRAND_PRINCIPLES",
            "ACCESSIBILITY_INFORMATION", "VOICE_AND_TONE", "CODE_EXAMPLE")),
    DEVICE(List.of("DESKTOP", "MOBILE"));

    private final List<String> values;

    FilterType(final List<String> values) {
        this.values = values;
    }

    public boolean contains(final String value) {
        return values.contains(value);
    }
}