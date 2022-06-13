package com.avanesov.email.registration.service;

import com.avanesov.email.app.appuser.AppUser;
import com.avanesov.email.app.appuser.AppUserRole;
import com.avanesov.email.app.service.AppUserService;
import com.avanesov.email.registration.request.RegistrationRequest;
import com.avanesov.email.registration.token.ConfirmationToken;
import com.avanesov.email.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator validator;
    private AppUserService appUserService;
    private ConfirmationTokenService confirmationTokenService;


    public String register(RegistrationRequest request) {
        boolean isValidEmail = validator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalArgumentException("email not valid");

        }
        return appUserService.singUpUser(new AppUser(request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                request.getEmail(),
                AppUserRole.USER));
    }


}
