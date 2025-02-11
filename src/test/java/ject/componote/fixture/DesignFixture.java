//package ject.componote.fixture;
//
//import ject.componote.domain.common.model.BaseImage;
//import ject.componote.domain.design.domain.Design;
//import ject.componote.domain.design.domain.filter.DesignFilter;
//import ject.componote.domain.design.domain.link.DesignLink;
//import ject.componote.domain.design.domain.filter.FilterType;
//import ject.componote.domain.design.domain.link.LinkType;
//import ject.componote.domain.design.model.Url;
//
//import java.util.List;
//
//public enum DesignFixture {
//    기본_디자인("기본 디자인", "Componote", "이것은 기본 디자인입니다.", "https://example.com/thumbnail1.png"),
//    추가_디자인("추가 디자인", "Componote", "이것은 추가적인 디자인입니다.", "https://example.com/thumbnail2.png");
//
//    private final String name;
//    private final String organization;
//    private final String description;
//    private final String thumbnailUrl;
//
//    DesignFixture(String name, String organization, String description, String thumbnailUrl) {
//        this.name = name;
//        this.organization = organization;
//        this.description = description;
//        this.thumbnailUrl = thumbnailUrl;
//    }
//
//    public Design 생성() {
//        return Design.of(name, organization, description, BaseImage.from(thumbnailUrl));
//    }
//
//    public static Design 기본_디자인_생성() {
//        return 기본_디자인.생성();
//    }
//
//    public static Design 추가_디자인_생성() {
//        return 추가_디자인.생성();
//    }
//
//    public static List<DesignFilter> 필터_리스트_생성(Long designId) {
//        return List.of(
//                DesignFilter.of(FilterType.DEVICE, "DEVICE", designId),
//                DesignFilter.of(FilterType.CONTENT, "CONTENT", designId)
//        );
//    }
//
//    public static List<DesignLink> 링크_리스트_생성(Long designId) {
//        return List.of(
//                DesignLink.of(Design.of("Sample", "Org", "Description", BaseImage.from("https://example.com/sample.png")),
//                        LinkType.FIGMA, Url.from("https://example.com/link1")),
//                DesignLink.of(Design.of("Sample", "Org", "Description", BaseImage.from("https://example.com/sample.png")),
//                        LinkType.CODE_PEN, Url.from("https://example.com/link2"))
//        );
//    }
//}
