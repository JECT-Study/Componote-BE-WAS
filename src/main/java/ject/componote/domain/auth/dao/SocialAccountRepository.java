package ject.componote.domain.auth.dao;

import ject.componote.domain.auth.domain.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    Optional<SocialAccount> findBySocialIdAndProviderType(final String socialId, final String providerType);
}
