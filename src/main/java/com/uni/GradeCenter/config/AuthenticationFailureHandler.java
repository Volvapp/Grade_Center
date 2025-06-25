package com.uni.GradeCenter.config;

import com.uni.GradeCenter.exception.AccountPendingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        logger.error("Authentication failed: " + exception.getMessage());

        String errorMessage = "Invalid username or password";

        if (exception.getCause() instanceof AccountPendingException) {
            errorMessage = "Account is pending approval";
        }

        String encodedMessage = URLEncoder.encode(errorMessage, "UTF-8");
        getRedirectStrategy().sendRedirect(request, response, "/users/login?error=true&message=" + encodedMessage);
    }
}
