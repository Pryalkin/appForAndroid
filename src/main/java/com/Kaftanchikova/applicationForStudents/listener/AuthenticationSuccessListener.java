package com.Kaftanchikova.applicationForStudents.listener;

import com.Kaftanchikova.applicationForStudents.entity.UserPrincipal;
import com.Kaftanchikova.applicationForStudents.service.LoginAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationSuccessListener {

    private LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event){
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal){
            UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

}

