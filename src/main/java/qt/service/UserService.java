package qt.service;

import java.util.Date;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qt.domain.QtUserDetails;
import qt.domain.User;
import qt.domain.UserProfile;
import qt.domain.Valid;
import qt.repository.UserRepository;

@Service("userService")
@Transactional(readOnly = false)
public class UserService {

	@Value("${expired.days}")
	private Integer expiredDays;
	@Autowired private UserRepository userRepository;
	
	public Valid save(User user)
	{
		Valid valid = new Valid();
		Pattern usernamePattern = Pattern.compile("[a-zA-Z]{1}[a-zA-Z0-9_]{2,30}");
		Pattern passwordPattern =  Pattern.compile("[a-zA-Z0-9]{6,30}");
		if (user.getUsername() == null||
		   !usernamePattern.matcher(user.getUsername()).matches()) {
			valid.setRet(0);
			valid.setMsg("用户名长度必须在2～30之间，用户名由字母数字下划线组成，且开头必须是字母");
			return valid;
		}
		if (user.getPassword() == null
			||!passwordPattern.matcher(user.getPassword()).matches()) {
			valid.setRet(0);
			valid.setMsg("密码由字母和数字构成，不能超过30位");
			return valid;
		}
        //todo: 其它字段检查
		User dbUser = userRepository.findByUsername(user.getUsername());
		if (user.getId() == null) {
			if (dbUser != null) {
				valid.setRet(0);
				valid.setMsg("该用户名已经存在");
				return valid;
			}
		}
		else
		{
			if(dbUser != null&&!user.getId().equals(dbUser.getId()))
			{
				valid.setRet(0);
				valid.setMsg("该用户名已经存在");
				return valid;
			}
		}

		userRepository.save(user);
		valid.setRet(1);
		return valid;
	}

	public UserProfile login(String username, String password,String ip) {
		UserProfile userProfile = new UserProfile();
		User user = userRepository.findByUsername(username);
		if (user != null) {
			if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
				user.setLastLoginDate(new Date());
				String token = user.getId() + "_" + new BCryptPasswordEncoder().encode(new Date().toString());
				user.setToken(token);
				user.setLastLoginDate(new Date());
				user.setLastIp(ip);
				userRepository.save(user);
				userProfile.setRet(1);
				userProfile.setName(user.getName());
				userProfile.setToken(token);
				userProfile.setPic(user.getPic());
			}

		}
		else
		{
			userProfile.setRet(0);
			userProfile.setMsg("用户名或密码错误");
		}
		return userProfile;

	}

	public User validToken(String token) {
		
		Long expiredMS = 7*24*3600*1000l; //过期的毫秒数，读取被配置文件，如果没有，默认7天过期
		if(expiredDays!=null)
		{
			expiredMS = expiredDays*24*3600*1000l;
		}
		User user = userRepository.findByToken(token);
		if(user!=null && user.getLastLoginDate().getTime() > new Date().getTime() - expiredMS)
		{
			return user;
		}
		else
		{
			return null;
		}
		
	}

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("不能找到用户" + username);
		}
		QtUserDetails userDetails = new QtUserDetails();
		userDetails.setUsername(user.getUsername());
		userDetails.setPassword(user.getPassword());
		return userDetails;
	}
}
