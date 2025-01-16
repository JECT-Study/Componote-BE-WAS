package ject.componote.domain.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ject.componote.domain.auth.model.Email;
import ject.componote.domain.auth.model.Nickname;
import ject.componote.domain.auth.model.ProfileImage;
import ject.componote.domain.auth.model.converter.EmailConverter;
import ject.componote.domain.auth.model.converter.NicknameConverter;
import ject.componote.domain.auth.model.converter.ProfileImageConverter;
import ject.componote.domain.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@DynamicUpdate
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    @Convert(converter = NicknameConverter.class)
    private Nickname nickname;

    @Column(name = "email", nullable = true)
    @Convert(converter = EmailConverter.class)
    private Email email;

    @Column(name = "job", nullable = false)
    @Enumerated(EnumType.STRING)
    private Job job;

    @Column(name = "profile_image", nullable = false)
    @Convert(converter = ProfileImageConverter.class)
    private ProfileImage profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "social_account_id", nullable = false)
    private Long socialAccountId;

    private Member(final Nickname nickname, final Job job, final ProfileImage profileImage, final Long socialAccountId) {
        this.nickname = nickname;
        this.job = job;
        this.profileImage = profileImage;
        this.role = Role.USER;
        this.socialAccountId = socialAccountId;
    }

    public static Member of(final String nickname, final String job, final String objectKey, final Long socialAccountId) {
        return new Member(
                Nickname.from(nickname),
                Job.from(job),
                ProfileImage.from(objectKey),
                socialAccountId
        );
    }

    public boolean equalsNickname(final Nickname nickname) {
        return this.nickname.equals(nickname);
    }

    public boolean equalsProfileImage(final ProfileImage profileImage) {
        return this.profileImage.equals(profileImage);
    }

    public boolean equalsEmail(final Email email) {
        return Objects.equals(this.email, email);
    }

    public void updateProfileImage(final ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public void updateNickname(final Nickname nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(final Email email) {
        this.email = email;
    }

    public boolean hasEmail() {
        return this.email != null;
    }
}
