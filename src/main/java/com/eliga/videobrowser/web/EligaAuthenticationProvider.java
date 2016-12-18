package com.eliga.videobrowser.web;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.eliga.videobrowser.model.User;
import com.eliga.videobrowser.repository.UserRepository;
import com.eliga.videobrowser.types.ROLE;

@Service
public class EligaAuthenticationProvider implements AuthenticationProvider {

	private static Logger logger = (Logger) LoggerFactory.getLogger(EligaAuthenticationProvider.class);

	@Autowired
	UserRepository userRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		if (!checkForAnyUsers(authentication)) {
			// database is empty so allow logins until at least one user is
			// created
			return createToken(name, password, ROLE.admin.toString());
		}
		User user = userRepository.findByUsernameAndPassword(name, password);
		if (user != null) {
			return createToken(name, password, user.getRole());
		}else {
			return null;
		}
	}

	public Authentication createToken(String name, String password, String role) {
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority(role));
		Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
		return auth;
	}

	private boolean checkForAnyUsers(Authentication authentication) {
		long numberOfUsers = userRepository.count();
		return (numberOfUsers > 0);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		logger.info("supports {}", authentication);
		return true;
	}
}
