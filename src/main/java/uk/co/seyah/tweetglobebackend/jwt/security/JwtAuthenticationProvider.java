package uk.co.seyah.tweetglobebackend.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uk.co.seyah.tweetglobebackend.jwt.JwtAuthenticationToken;
import uk.co.seyah.tweetglobebackend.model.graph.object.User;
import uk.co.seyah.tweetglobebackend.jwt.JwtUserDto;
import uk.co.seyah.tweetglobebackend.jwt.exception.JwtTokenMalformedException;
import uk.co.seyah.tweetglobebackend.service.CustomUserDetailsService;

import java.util.List;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    @Autowired
    public JwtAuthenticationProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected User retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        JwtUserDto parsedUser = jwtTokenValidator.parseToken(token);

        if (parsedUser == null) {
            throw new JwtTokenMalformedException("JWT token is not valid");
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getRole());

        return customUserDetailsService.loadUserByUsername(parsedUser.getUsername());
    }

}
