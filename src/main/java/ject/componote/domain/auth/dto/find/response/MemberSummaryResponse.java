package ject.componote.domain.auth.dto.find.response;

import ject.componote.domain.member.dao.MemberSummaryDao;

public record MemberSummaryResponse(boolean isEmailRegistered, String nickname, String profileImageUrl) {
    public static MemberSummaryResponse from(final MemberSummaryDao memberSummaryDao) {
        return new MemberSummaryResponse(
                memberSummaryDao.email() != null,   // 좀 애매한 것 같음
                memberSummaryDao.nickname().getValue(),
                memberSummaryDao.profileImage().toUrl()
        );
    }
}
