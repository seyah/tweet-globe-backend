package uk.co.seyah.tweetglobebackend.rest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.seyah.tweetglobebackend.model.user.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @ResponseBody
    public User currentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

}
