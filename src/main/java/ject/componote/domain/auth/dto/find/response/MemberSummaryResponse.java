package ject.componote.domain.auth.dto.find.response;

import ject.componote.domain.auth.dao.MemberSummaryDao;

public record MemberSummaryResponse(String nickname, String profileImageUrl) {
    public static MemberSummaryResponse from(MemberSummaryDao memberSummaryDao) {
        return new MemberSummaryResponse(
                memberSummaryDao.nickname().getValue(),
                memberSummaryDao.profileImage().toUrl()
        );
    }
}
