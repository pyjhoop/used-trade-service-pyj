package com.example.trade.service;

import com.example.trade.domain.UserAccount;
import com.example.trade.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("실행되네?");
        System.out.println("============");

//        return userAccountRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException(username+"을 데이터베이스에서 찾을수 없습니다."));
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(username);
        System.out.println(username);

        if(userAccount.isPresent()){
            System.out.println(userAccount.get());
            return userAccount.get();
        }else{
            throw new UsernameNotFoundException("데이터베이스에 없습니다.");
        }
    }
}
