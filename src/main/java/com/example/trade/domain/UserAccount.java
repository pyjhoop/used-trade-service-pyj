package com.example.trade.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(indexes = {
        @Index(columnList = "email")
})
@Entity
public class UserAccount extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter private String userPassword;
    @Setter private String name;
    @Setter private String nickname;
    @Setter private String role;
    @Setter private String profile;

    public UserAccount(String email, String userPassword, String name, String nickname, String role, String profile) {
        this.email = email;
        this.userPassword = userPassword;
        this.name = name;
        this.nickname = nickname;
        this.role = role;
        this.profile = profile;
    }
    public static UserAccount of(String email, String userPassword, String name, String nickname, String role, String profile) {
        return new UserAccount(email, userPassword, name,nickname, role, profile);
    }

    public UserAccount update(String name) {
        this.name = name;
        return this;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
