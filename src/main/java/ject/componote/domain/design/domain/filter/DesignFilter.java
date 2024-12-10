package ject.componote.domain.design.domain.filter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class DesignFilter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FilterType type;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "design_id", nullable = false)
    private Long designId;

    public DesignFilter(final FilterType type, final String value, final Long designId) {
        validateValue(type, value);
        this.type = type;
        this.value = value;
        this.designId = designId;
    }

    public static DesignFilter of(final FilterType type, final String value, final Long designId) {
        return new DesignFilter(type, value, designId);
    }

    private void validateValue(final FilterType type, final String value) {
        if (!type.contains(value)) {
            throw new IllegalArgumentException(String.format("Value '%s' is not a valid value for type '%s'", value, type));
        }
    }
}
