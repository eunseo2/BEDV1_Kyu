package org.prgrms.kyu.service;


import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.dto.JoinRequest;
import org.prgrms.kyu.dto.LoginRequest;
import org.prgrms.kyu.dto.UserInfo;
import org.prgrms.kyu.entity.User;
import org.prgrms.kyu.entity.UserType;
import org.prgrms.kyu.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final SecurityService securityService;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long join(JoinRequest form) {
        String originalPassword = form.getPassword();
        form.encodePassword(encoder.encode(form.getPassword()));
        User user = new User(form);

        final User savedUser = repository.save(user);
        //회원가입 후 자동 로그인을 위해 form 복원
        form.setPassword(originalPassword);
        return savedUser.getId();
    }

    public void autoLogin(JoinRequest joinRequest) {
        securityService.autoLogin(joinRequest.getEmail(), joinRequest.getPassword());
    }

    public UserInfo login(LoginRequest loginRequest) throws AuthenticationException {
        final Optional<User> optionalUser = repository.findByEmail(loginRequest.getEmail());
        if (optionalUser.isEmpty() || !encoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
            throw new AuthenticationException("회원을 찾을 수 없습니다.");
        }
        return new UserInfo(optionalUser.get());
    }

    public UserInfo getUser(String email) {
        return new UserInfo(repository.getUserByEmail(email));
    }

    public User findById(Long id){
        return repository.getById(id);
  }

  public UserType getUserType(String email){
        return repository.getUserByEmail(email).getType();
  }
}
