//package com.example.first.config;
/**
 * spring security 사용 시 계정정도 저장
 */
//
//import com.example.first.entity.User;
//import com.example.first.exception.UserNotFound;
//import com.example.first.repository.MybatisUserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class CustomUserDetailConfig implements UserDetailsService {
//
//    private final MybatisUserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(User.builder().email(username).build()).orElseThrow(UserNotFound::new);
//        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())
//                .authorities(List.of())
//                .build();
//        return userDetails;
//    }
//}
