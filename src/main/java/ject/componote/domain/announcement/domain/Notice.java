package ject.componote.domain.announcement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.announcement.model.Description;
import ject.componote.domain.announcement.model.Title;
import ject.componote.domain.announcement.model.converter.DescriptionConverter;
import ject.componote.domain.announcement.model.converter.TitleConverter;
import ject.componote.domain.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TitleConverter.class)
    @Column(name = "title", nullable = false)
    private Title title;

    @Convert(converter = DescriptionConverter.class)
    @Column(name = "description", nullable = false)
    private Description description;
}
