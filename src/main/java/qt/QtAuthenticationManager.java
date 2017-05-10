package qt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import qt.service.UserService;

@Service
@Qualifier("authenticationManager")
public class QtAuthenticationManager implements AuthenticationManager {

	@Autowired
	private UserService userService;

	public QtAuthenticationManager() {
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

		String username = token.getName(); 
		String password = (String) token.getCredentials();

		UserDetails userDetails = userService.loadUserByUsername(username);
		if (password.equals(userDetails.getPassword()))
			return authenticatedToken(userDetails, authentication);
		else
			throw new UsernameNotFoundException("用户名或密码错误！");

	}

	private Authentication authenticatedToken(UserDetails userDetails, Authentication original) {
		UsernamePasswordAuthenticationToken authenticated = new UsernamePasswordAuthenticationToken(userDetails,
				userDetails.getPassword(), userDetails.getAuthorities());
		return authenticated;
	}
}
