package ject.componote.domain.bookmark.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.common.domain.BaseEntity;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.domain.DesignSystem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ComponentBookmark  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    private ComponentBookmark(final Long memberId, final Long componentId) {
        this.memberId = memberId;
        this.componentId = componentId;
    }

    public static ComponentBookmark of(final Member member, final Component component) {
        return new ComponentBookmark(member.getId(), component.getId());
    }
}
