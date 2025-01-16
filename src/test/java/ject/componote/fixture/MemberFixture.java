package ject.componote.fixture;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.model.Email;

public enum MemberFixture {
    KIM("김민우", null, "DEVELOPER", "profiles/123.jpg"),
    이메일O_회원("김민우", "rlarla677@gmail.com", "DEVELOPER", "profiles/123.jpg"),
    이메일X_회원("김민우", null, "DEVELOPER", "profiles/123.jpg");

    private final String nickname;
    private final String email;
    private final String job;
    private final String objectKey;

    MemberFixture(final String nickname, final String email, final String job, final String objectKey) {
        this.nickname = nickname;
        this.email = email;
        this.job = job;
        this.objectKey = objectKey;
    }

    public Member 생성(final Long socialAccountId) {
        final Member member = Member.of(nickname, job, objectKey, socialAccountId);
        if (this.email != null) {
            member.updateEmail(Email.from(email));
        }

        return member;
    }
}
