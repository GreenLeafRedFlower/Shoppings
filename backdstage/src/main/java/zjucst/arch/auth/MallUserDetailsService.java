package zjucst.arch.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import zjucst.arch.Exception.MallException;
import zjucst.arch.domain.entity.User;
import zjucst.arch.repository.UserRepository;

import javax.transaction.Transactional;

@Component("userDetailsService")
public class MallUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public MallUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws MallException {
        User u = userRepository.findByUsername(username);
        if (u == null) {
            throw new MallException(10002, "the username '" + username + "' is not existed");
        }
        return new MallUserPrincipal(u.getId(), u.getUsername(), u.getPassword());
    }
}
