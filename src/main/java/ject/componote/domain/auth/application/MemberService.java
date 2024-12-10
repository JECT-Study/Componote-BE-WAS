package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dto.find.response.MemberSummaryResponse;
import ject.componote.domain.auth.model.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberFindService memberFindService;

    public MemberSummaryResponse getSummary(final AuthPrincipal authPrincipal) {
        return memberFindService.findSummaryByMemberId(authPrincipal.id());
    }
}
