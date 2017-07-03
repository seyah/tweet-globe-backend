package uk.co.seyah.tweetglobebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uk.co.seyah.tweetglobebackend.model.user.User;
import uk.co.seyah.tweetglobebackend.model.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	IUserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepo.findOneByUsername(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Unknown username: " + userName);
		}

		return user;
	}

	public User loadUserByEmail(String email) throws UsernameNotFoundException {
		User user = userRepo.findOneByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("Unknown email: " + email);
		}
		return user;
	}

	public User saveUser(User user){
		return userRepo.save(user);
	}

	public boolean checkPassword(UserDetails user, String password){
		return passwordEncoder.matches(password, user.getPassword());
	}

	public List<GrantedAuthority> getAuthorities(Integer role) {
		List<GrantedAuthority> authList = new ArrayList<>();
		if (role == 1) {
			authList.add(new SimpleGrantedAuthority("ROLE_USER"));
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else if (role == 2) {
			authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authList;
	}

	public User registerNewUserAccount(UserDto accountDto) throws Exception {

		if (emailExist(accountDto.getEmail())) {
			throw new Exception("There is an account with that email address: " + accountDto.getEmail());
		}
		if (usernameExist(accountDto.getUsername())) {
			throw new Exception("There is an account with that username: " + accountDto.getEmail());
		}
		User user = new User();
		user.setFirstName(accountDto.getFirstName());
		user.setLastName(accountDto.getLastName());
		user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
		user.setUsername(accountDto.getUsername());
		user.setEmail(accountDto.getEmail());
		user.setRole(2);
		return userRepo.save(user);
	}

	private boolean emailExist(String email) {
		User user = userRepo.findOneByEmail(email);
		return user != null;
	}

	private boolean usernameExist(String username) {
		User user = userRepo.findOneByUsername(username);
		return user != null;
	}
}
