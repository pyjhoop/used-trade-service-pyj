package com.example.trade.dto.response;

import com.example.trade.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoWithToken {
    private String email;
    private String name;
    private String nickname;
    private String profile;
    private String accessToken;

    public UserInfoWithToken(String email, String name, String nickname, String profile) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.profile = profile;
    }

    public static UserInfoWithToken from(UserInfoWithTokens userInfo){
        return new UserInfoWithToken(
                userInfo.getEmail(),
                userInfo.getName(),
                userInfo.getNickname(),
                userInfo.getProfile(),
                userInfo.getAccessToken()
        );

    }
}
