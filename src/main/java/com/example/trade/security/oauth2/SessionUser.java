package com.example.trade.security.oauth2;

import com.example.trade.domain.UserAccount;
import lombok.Getter;

@Getter
public class SessionUser {
    private String name;
    private String email;

    public SessionUser(UserAccount user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}