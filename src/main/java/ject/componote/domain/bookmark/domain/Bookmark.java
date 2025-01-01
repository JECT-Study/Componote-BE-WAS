package ject.componote.domain.bookmark.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.common.domain.BaseEntity;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.design.domain.DesignSystem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Column(name = "type", nullable = false)
    private String type;

    private Bookmark(final Long memberId, final Long resourceId, final String type) {
        this.memberId = memberId;
        this.resourceId = resourceId;
        this.type = type;
    }

    public static Bookmark of(final Member member, final Component component) {
        return new Bookmark(member.getId(), component.getId(), "component");
    }

    public static Bookmark of(final Member member, final DesignSystem designSystem) {
        return new Bookmark(member.getId(), designSystem.getDesign().getId(), "designSystem");
    }
}
