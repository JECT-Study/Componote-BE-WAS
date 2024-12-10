package ject.componote.domain.design.domain.link;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.design.domain.Design;
import ject.componote.domain.design.model.Url;
import ject.componote.domain.design.model.converter.UrlConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class DesignLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "design_id", nullable = false)
    private Long designId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LinkType type;

    @Convert(converter = UrlConverter.class)
    @Column(name = "url", nullable = false)
    private Url url;

    private DesignLink(final Long designId, final LinkType type, final Url url) {
        validate(type, url.getValue());
        this.designId = designId;
        this.type = type;
        this.url = url;
    }

    public static DesignLink of(final Design design, final LinkType type, final Url url) {
        return new DesignLink(design.getId(), type, url);
    }

    private void validate(final LinkType type, final String urlValue) {
        if (!type.validate(urlValue)) {
            throw new IllegalArgumentException("Invalid URL for the given type: " + type);
        }
    }
}
