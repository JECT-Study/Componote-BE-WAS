package ject.componote.domain.auth.application;

import ject.componote.domain.auth.dao.MemberRepository;
import ject.componote.domain.auth.domain.SocialAccount;
import ject.componote.domain.auth.dto.login.response.OAuthLoginResponse;
import ject.componote.domain.auth.dto.authorize.response.OAuthAuthorizationUrlResponse;
import ject.componote.domain.auth.util.AESCryptography;
import ject.componote.infra.oauth.application.OAuthClient;
import ject.componote.infra.oauth.dto.authorize.response.OAuthAuthorizePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final AESCryptography aesCryptography;
    private final MemberRepository memberRepository;
    private final OAuthClient oAuthClient;
    private final OAuthResultHandler oauthResultHandler;

    public OAuthAuthorizationUrlResponse getOAuthAuthorizationCodeUrl(final String providerType) {
        final OAuthAuthorizePayload oAuthAuthorizePayload = oAuthClient.getAuthorizePayload(providerType);
        return OAuthAuthorizationUrlResponse.from(oAuthAuthorizePayload);
    }

    public OAuthLoginResponse login(final String providerType, final String code) {
        final SocialAccount socialAccount = oAuthClient.getProfile(providerType, code)
                .publishOn(Schedulers.boundedElastic())
                .map(oauthResultHandler::saveOrGet)
                .block();
        final Long socialAccountId = socialAccount.getId();
        final boolean isRegister = memberRepository.existsBySocialAccountId(socialAccountId);
        return OAuthLoginResponse.of(isRegister, aesCryptography.encrypt(socialAccountId));
    }
}
