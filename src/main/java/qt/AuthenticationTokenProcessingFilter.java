package qt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import qt.domain.User;
import qt.service.UserService;

@Component
public class AuthenticationTokenProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public AuthenticationTokenProcessingFilter() {
	}

	@Autowired
	public AuthenticationTokenProcessingFilter(
			@Qualifier("authenticationManager") AuthenticationManager authenticationManager) {
		setAuthenticationManager(authenticationManager);
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		return null;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (req instanceof org.apache.catalina.connector.RequestFacade) {
			chain.doFilter(request, response);

		} else {
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				String token = request.getHeader("TOKEN");
				if (token != null) {
					User user =userService.validToken(token);
					if (user!=null) {
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								user.getUsername(), user.getPassword());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						Authentication authenticated = authenticationManager.authenticate(authentication);
						SecurityContextHolder.getContext().setAuthentication(authenticated);
					}
				}

			}
			chain.doFilter(request, response);
		}
	}

}