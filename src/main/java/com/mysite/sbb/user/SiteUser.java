package com.mysite.sbb.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String sns;

    private String picture;

    public SiteUser update(String username, String picture) {
        this.username = username;
        this.picture = picture;

        return this;
    }

    @Builder
    public SiteUser(String username, String email, String sns, String picture) {
        this.username = username;
        this.email = email;
        this.sns = sns;
        this.picture = picture;
    }
}
