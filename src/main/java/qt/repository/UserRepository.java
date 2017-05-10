package qt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qt.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);
	public User findByToken(String token);

}
