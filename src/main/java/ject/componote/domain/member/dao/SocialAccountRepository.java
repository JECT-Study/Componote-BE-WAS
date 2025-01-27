package ject.componote.domain.member.dao;

import ject.componote.domain.member.domain.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findBySocialIdAndProviderType(final String socialId, final String providerType);
}
