package com.example.trade.dto.request;

import com.example.trade.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String profile;

    public UserAccount toEntity(){
        return UserAccount.of(email, password, name, nickname, "ROLE_USER", profile);
    }
}
