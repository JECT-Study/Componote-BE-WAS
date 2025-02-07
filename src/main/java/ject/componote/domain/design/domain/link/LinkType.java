package ject.componote.domain.design.domain.link;

public enum LinkType {
    GITHUB("^https://github\\.com/.+"),
    FIGMA("^https://www\\.figma\\.com/.+"),
    SLACK("^https://[a-zA-Z0-9]+\\.slack\\.com/.+"),
    CODE_PEN("^https://codepen\\.io/.+"),
    WEBSITE(".*"),
    ZEROHEIGHT("^https://base\\.uber\\.com/.+"),
    STORYBOOK("^https://[a-zA-Z0-9.-]+\\.storybook\\.com/.+|^https://[a-zA-Z0-9.-]+\\.delldesignsystem\\.com/.+"),
    ETC(".*"); // ETC는 모든 URL 허용

    private final String regex;

    LinkType(String regex) {
        this.regex = regex;
    }

    public boolean validate(String url) {
        return url.matches(this.regex);
    }
}
