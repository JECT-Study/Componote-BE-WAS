package ject.componote.domain.component.domain.summary;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import ject.componote.domain.common.model.BaseImage;
import ject.componote.domain.common.model.converter.BaseImageConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ComponentSummary {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Convert(converter = BaseImageConverter.class)
    @Column(name = "thumbnail", nullable = false)
    private BaseImage thumbnail;

    private ComponentSummary(final String title, final String summary, final BaseImage thumbnail) {
        validateTitle(title);
        validateSummary(summary);
        this.title = title;
        this.summary = summary;
        this.thumbnail = thumbnail;
    }

    private void validateTitle(final String title) {

    }

    private void validateSummary(final String summary) {

    }

    public static ComponentSummary of(final String title, final String summary, final BaseImage thumbnail) {
        return new ComponentSummary(title, summary, thumbnail);
    }
}
