package com.avanesov.email.app.service;

import com.avanesov.email.app.appuser.AppUser;
import com.avanesov.email.app.repository.UserRepository;
import com.avanesov.email.registration.token.ConfirmationToken;
import com.avanesov.email.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND = "User with email %s not found";
    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
    }

    public String singUpUser(AppUser user) {
        boolean userExist = repository.findByEmail(user.getEmail()).isPresent();
        if (userExist) {
            throw new IllegalStateException("Email already taken");
        }
        String encoderPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encoderPassword);
        repository.save(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }


}
