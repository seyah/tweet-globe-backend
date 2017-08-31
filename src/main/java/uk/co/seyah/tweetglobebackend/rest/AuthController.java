package uk.co.seyah.tweetglobebackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.seyah.tweetglobebackend.jwt.JwtAuthenticationToken;
import uk.co.seyah.tweetglobebackend.jwt.JwtUserDto;
import uk.co.seyah.tweetglobebackend.model.Message;
import uk.co.seyah.tweetglobebackend.model.user.Credentials;
import uk.co.seyah.tweetglobebackend.model.user.User;
import uk.co.seyah.tweetglobebackend.model.dto.UserDto;
import uk.co.seyah.tweetglobebackend.jwt.security.JwtAuthenticationProvider;
import uk.co.seyah.tweetglobebackend.jwt.security.JwtTokenGenerator;
import uk.co.seyah.tweetglobebackend.service.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Credentials credentials, HttpServletRequest request) {

        if (credentials.getEmail() == null || credentials.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Please enter an email and password!", "login.error.badLogin", 1));
        }

        String email = credentials.getEmail();

        User user;
        try {
            user = userDetailsService.loadUserByEmail(email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(new Message("E-mail not found.", "login.error.badLogin", 1));
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("E-mail not found.", "login.error.badLogin", 1));
        }

        if (!userDetailsService.checkPassword(user, credentials.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Invalid login. Please check your name and password.", "login.error.badLogin", 1));
        }

        JwtUserDto u = new JwtUserDto();
        u.setId(123L);
        u.setUsername(user.getUsername());
        u.setRole("admin");
        String token = JwtTokenGenerator.generateToken(u, "my-very-secret-key");

        //user.setAuthenticated(true);
        //userDetailsService.saveUser(user);

        authenticationProvider.authenticate(new JwtAuthenticationToken(token));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new LoginResponse(token, true));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody UserDto accountDto) {
        User registered;
        try {
            registered = userDetailsService.registerNewUserAccount(accountDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Message("Username or e-mail already exists!", "register.error.badRegister", 1));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Message("Thanks " + registered.getFirstName() + "! Registered successfully.", "register.success.registered", 0));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public void logout(HttpSession session) {
        session.invalidate();
    }

    private class LoginResponse {

        private String token;
        private boolean isAuthenticated;

        public LoginResponse(String token, boolean isAuthenticated){
            this.token = token;
            this.isAuthenticated = isAuthenticated;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isAuthenticated() {
            return isAuthenticated;
        }

        public void setAuthenticated(boolean authenticated) {
            isAuthenticated = authenticated;
        }
    }
}