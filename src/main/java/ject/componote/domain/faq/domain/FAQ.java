package ject.componote.domain.faq.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.faq.model.FAQContent;
import ject.componote.domain.faq.model.FAQTitle;
import ject.componote.domain.faq.model.converter.FAQContentConverter;
import ject.componote.domain.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class FAQ extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FAQType type;

    @Convert(converter = FAQTitle.class)
    @Column(name = "title", nullable = false)
    private FAQTitle title;

    @Convert(converter = FAQContentConverter.class)
    @Column(name = "content", nullable = false)
    private FAQContent content;
}
