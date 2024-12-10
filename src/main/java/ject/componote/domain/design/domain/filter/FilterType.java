package ject.componote.domain.design.domain.filter;

import lombok.Getter;

import java.util.List;

@Getter
public enum FilterType {
    PLATFORM(List.of("GITHUB", "FIGMA", "STORYBOOK", "ZEROHEIGHT")),
    TECHNOLOGY(List.of("REACT", "ANGULAR", "VUE")),
    CONTENT(List.of("DESIGN_TOKEN", "CODE_EXAMPLES", "ACCESSIBILITY_GUIDE")),
    DEVICE(List.of("DESKTOP", "MOBILE"));

    private final List<String> values;

    FilterType(final List<String> values) {
        this.values = values;
    }

    public boolean contains(final String value) {
        return values.contains(value);
    }
}