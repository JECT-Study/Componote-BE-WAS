package ject.componote.domain.bookmark.domain;

import jakarta.persistence.*;
import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.common.domain.BaseEntity;
import ject.componote.domain.component.domain.Component;
import ject.componote.domain.design.domain.Design;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class DesignSystemBookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "design_id", nullable = false)
    private Long designId;

    private DesignSystemBookmark(final Long memberId, final Long designId) {
        this.memberId = memberId;
        this.designId = designId;
    }

    public static DesignSystemBookmark of(final Member member, final Design design) {
        return new DesignSystemBookmark(member.getId(), design.getId());
    }
}
