package uk.co.seyah.tweetglobebackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uk.co.seyah.tweetglobebackend.model.exception.ErrorMessage;
import uk.co.seyah.tweetglobebackend.model.Credentials;
import uk.co.seyah.tweetglobebackend.model.User;
import uk.co.seyah.tweetglobebackend.model.dto.UserDto;
import uk.co.seyah.tweetglobebackend.service.CustomUserDetailsService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController()
@RequestMapping("/auth")
@CrossOrigin(origins="*", maxAge=3600)
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User login(@RequestBody Credentials credentials, HttpSession httpSession) {
        User user = new User(credentials.getUsername(), httpSession.getId(), true);
        httpSession.setAttribute("user", user);
        return user;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@ModelAttribute("user") @Valid UserDto accountDto) {
        User registered;
        try {
            registered = userDetailsService.registerNewUserAccount(accountDto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(e.getMessage(), "register.error.badRegister"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(registered);
    }

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public User session(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public void logout(HttpSession session) {
        session.invalidate();
    }
}