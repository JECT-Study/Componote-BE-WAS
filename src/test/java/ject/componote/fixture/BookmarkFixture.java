package ject.componote.fixture;

import ject.componote.domain.member.domain.Member;
import ject.componote.domain.bookmark.domain.Bookmark;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.design.domain.Design;

public enum BookmarkFixture {
  COMPONENT_BOOKMARK(1L, "component"),
  DESIGN_SYSTEM_BOOKMARK(2L, "designSystem");

  private final Long resourceId;
  private final String type;

  BookmarkFixture(final Long resourceId, final String type) {
    this.resourceId = resourceId;
    this.type = type;
  }

  public Bookmark 컴포넌트_북마크_생성(final Member member, final Component component) {
    return Bookmark.of(member, component);
  }

  public Bookmark 디자인시스템_북마크_생성(final Member member, final Design designSystem) {
    return Bookmark.of(member, designSystem);
  }

  public Long getResourceId() {
    return resourceId;
  }

  public String getType() {
    return type;
  }
}
