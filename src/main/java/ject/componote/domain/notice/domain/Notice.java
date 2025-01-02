package ject.componote.domain.notice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.common.domain.BaseEntity;
import ject.componote.domain.notice.model.NoticeContent;
import ject.componote.domain.notice.model.NoticeTitle;
import ject.componote.domain.notice.model.converter.NoticeContentConverter;
import ject.componote.domain.notice.model.converter.NoticeTitleConverter;
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

    @Convert(converter = NoticeTitleConverter.class)
    @Column(name = "title", nullable = false)
    private NoticeTitle title;

    @Convert(converter = NoticeContentConverter.class)
    @Column(name = "content", nullable = false)
    private NoticeContent content;
}
