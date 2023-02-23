package com.mysite.sbb.user;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
        throws OAuth2AuthenticationException {

        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId =
            userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName =
            userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        log.info("ClientRegistration : " + registrationId);
        log.info("AccessToken : " + accessToken);

        OAuthAttributes attributes = OAuthAttributes.of(registrationId,
            userNameAttributeName,
            oAuth2User.getAttributes());

        SiteUser oAuthAccount = saveOrUpdate(attributes);

        return new PrincipalDetails(oAuthAccount, attributes.getAttributes());
    }

    private SiteUser saveOrUpdate(OAuthAttributes attributes) {
        SiteUser oAuthAccount =
            userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getUsername(),
                    attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(oAuthAccount);
    }
}
