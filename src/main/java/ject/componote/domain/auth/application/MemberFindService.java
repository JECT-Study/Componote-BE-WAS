package ject.componote.domain.auth.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.domain.MemberRepository;
import ject.componote.domain.auth.dto.find.response.MemberSummaryResponse;
import ject.componote.domain.auth.error.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberFindService {
    private final MemberRepository memberRepository;

    public Optional<Member> findOptionalBySocialId(final String socialId) {
        return memberRepository.findBySocialId(socialId);
    }

    public MemberSummaryResponse findSummaryByMemberId(final Long memberId) {
        return memberRepository.findSummaryById(memberId)
                .map(MemberSummaryResponse::from)
                .orElseThrow(() -> new NotFoundMemberException(memberId));
    }
}
