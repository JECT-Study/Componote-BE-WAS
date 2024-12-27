package ject.componote.fixture;

import ject.componote.domain.auth.domain.Member;

public enum MemberFixture {
    KIM("김민우", "DEVELOPER", "profiles/123.jpg");

    private final String nickname;
    private final String job;
    private final String objectKey;

    MemberFixture(final String nickname, final String job, final String objectKey) {
        this.nickname = nickname;
        this.job = job;
        this.objectKey = objectKey;
    }

    public Member 생성(final Long socialAccountId) {
        return Member.of(nickname, job, objectKey, socialAccountId);
    }
}
