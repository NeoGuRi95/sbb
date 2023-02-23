package com.mysite.sbb.user;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String username;
    private String email;
    private String picture;
    private String sns;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
        String nameAttributeKey, String username, String email, String picture,
        String sns) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.sns = sns;
    }

    public static OAuthAttributes of(String registrationId,
        String userNameAttributeName,
        Map<String, Object> attributes) {
        if (registrationId.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofNaver(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .username((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("picture"))
            .sns("kakao")
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .username((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("picture"))
            .sns("naver")
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    public SiteUser toEntity() {
        return SiteUser.builder()
            .username(username)
            .email(email)
            .picture(picture)
            .sns(sns)
            .build();
    }
}
