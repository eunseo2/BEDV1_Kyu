package org.prgrms.kyu.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.prgrms.kyu.dto.JoinRequest;
import org.prgrms.kyu.dto.LoginRequest;
import org.prgrms.kyu.dto.UserInfo;
import org.prgrms.kyu.entity.User;
import org.prgrms.kyu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.naming.AuthenticationException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private BCryptPasswordEncoder encoder;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    void 회원가입() {
        //given
        JoinRequest joinRequest = new JoinRequest("test@test.com", "1234", "user1", "nick1", "Seoul", "CUSTOMER");

        //when
        final Long userId = userService.join(joinRequest);

        //then
        final Optional<User> foundUser = userRepository.findById(userId);
        Assertions.assertThat(foundUser).isNotEmpty();
        assertThat(foundUser.get().getEmail(), is(joinRequest.getEmail()));
    }

    @Test
    void 로그인() throws AuthenticationException {
        //given
        JoinRequest joinRequest = new JoinRequest("test@test.com", "1234", "user1", "nick1", "Seoul", "CUSTOMER");
        joinRequest.encodePassword(encoder.encode(joinRequest.getPassword()));
        final User savedUser = userRepository.save(new User(joinRequest));
        final UserInfo savedUserInfo = new UserInfo(savedUser);
        LoginRequest loginRequest = new LoginRequest("test@test.com", "1234");

        //when
        final UserInfo userInfo = userService.login(loginRequest);

        //then
        assertThat(userInfo, is(samePropertyValuesAs(savedUserInfo)));
    }
}