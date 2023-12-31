package com.example.trade.dto.response;

import com.example.trade.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoWithTokens {
    private String email;
    private String name;
    private String nickname;
    private String profile;
    private String accessToken;
    private String refreshToken;

    public UserInfoWithTokens(String email, String name, String nickname, String profile) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profile = profile;
    }

    public static UserInfoWithTokens from(UserAccount userAccount){
        return new UserInfoWithTokens(userAccount.getEmail(), userAccount.getName(), userAccount.getNickname(), userAccount.getProfile());

    }
}
