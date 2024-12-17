package ject.componote.fixture;

import ject.componote.domain.auth.domain.Member;

public enum MemberFixture {
    KIM("김민우", "rlarla677@gmail.com", "DEVELOPER", "permanent/123.jpg");

    private final String nickname;
    private final String email;
    private final String job;
    private final String profileImage;

    MemberFixture(final String nickname, final String email, final String job, final String profileImage) {
        this.nickname = nickname;
        this.email = email;
        this.job = job;
        this.profileImage = profileImage;
    }

    public Member 생성(final Long socialAccountId) {
        return Member.of(nickname, email, job, profileImage, socialAccountId);
    }
}
