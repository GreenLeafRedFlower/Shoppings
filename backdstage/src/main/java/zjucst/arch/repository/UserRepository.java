package zjucst.arch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import zjucst.arch.domain.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    @Transactional
    List<User> deleteByUsername(String username);
}
