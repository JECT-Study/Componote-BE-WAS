package ject.componote.domain.auth.application;

import ject.componote.domain.auth.domain.Member;
import ject.componote.domain.auth.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberSaveService {
    private final MemberRepository memberRepository;

    public Member save(final Member member) {
        return memberRepository.save(member);
    }
}
