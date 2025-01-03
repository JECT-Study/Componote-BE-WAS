package ject.componote.domain.component.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import ject.componote.domain.design.domain.Design;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ComponentDesign {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "component_id", nullable = false)
    private Long componentId;

    @Column(name = "design_id", nullable = false)
    private Long designId;

    private ComponentDesign(final Long componentId, final Long designId) {
        this.componentId = componentId;
        this.designId = designId;
    }

    public static ComponentDesign from(final Component component, final Design design) {
        return new ComponentDesign(component.getId(), design.getId());
    }
}
