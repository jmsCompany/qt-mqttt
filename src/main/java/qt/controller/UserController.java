package qt.controller;


import java.util.Date;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qt.domain.User;
import qt.domain.UserProfile;
import qt.domain.Valid;
import qt.service.UserService;


@RestController
@Transactional(readOnly = false)
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserProfile login(@RequestParam String username,@RequestParam String password, ServletRequest req) {
		HttpServletRequest request = (HttpServletRequest) req;
		String ip = request.getRemoteAddr();
		return userService.login(username, password,ip);
	}


	@RequestMapping(value = "/reg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Valid reg(@RequestBody User user, ServletRequest req) {
		HttpServletRequest request = (HttpServletRequest) req;
		String ip = request.getRemoteAddr();
		user.setCreationIp(ip);
		user.setId(null); 
		user.setPassword(new BCryptPasswordEncoder().encode(user.getUsername()));
		user.setCreationDate(new Date());
		user.setLastLoginDate(new Date());
		return userService.save(user);
		
	}




}
