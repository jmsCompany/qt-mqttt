package qt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Configuration
@EnableWebSecurity
public class QtApplicationSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/login").permitAll().antMatchers("/reg/**").permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
		http.addFilter(authenticationTokenProcessingFilter);
		http.authorizeRequests().anyRequest().authenticated();

	}

}