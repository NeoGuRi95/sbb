package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class PrincipalDetails implements UserDetails, OAuth2User {
    private SiteUser siteUser;
    private Map<String, Object> attributes;

    // 일반 유저 로그인 시 사용하는 생성자
    public PrincipalDetails(SiteUser siteUser) {
        this.siteUser = siteUser;
    }

    // OAuth2User 를 사용한 SNS 유저 로그인 시 사용하는 생성자
    public PrincipalDetails(SiteUser siteUser, Map<String, Object> attributes) {
        this.siteUser = siteUser;
        this.attributes = attributes;
    }

    // 해당 유저의 권한을 리턴하는 곳
    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(siteUser.getUsername())) {
            authorities
                .add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities
                .add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return authorities;
    }

    // 해당 유저의 패스워드 리턴
    @Override
    public String getPassword() {
        return siteUser.getPassword();
    }

    // 해당 유저의 mid 리턴
    @Override
    public String getUsername() {
        return siteUser.getUsername();
    }

    // 계정 만료가 아니니?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠긴게 아니니?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 정보 변경해야하는거 아니니?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 되어있니?
    @Override
    public boolean isEnabled() {
        // 예를 들어서 사이트에서 1년동안 회원이 로그인 안하면
        // 해당 계정 휴면 계정으로 전환하는 규정같은 것들이 있을때 사용!!
        // 현재시간 - 로긴시간 => 1년 초과시 return false
        return true;
    }

    // OAuth2User 즉 sns 로그인 유저의 oauth2user 의 Attributes 정보를 확인하기 위한 메서드
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public String getName() {
        return siteUser.getUsername();
    }
}
