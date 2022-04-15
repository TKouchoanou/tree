package malo.bloc.tree.security.jwt;

import malo.bloc.tree.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("javainuse".equals(username)) {
             return  new User("javainuse","$2a$10$cIMDmrhN2tL7EYL1qIoKfOr.wmvWo.rD6MepPFEiNd73EKvdikyy.",new ArrayList<>());
        }
        Optional<malo.bloc.tree.persistence.entity.User> user =userRepository.findByEmail(username);
        if(user.isPresent()){
            // SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("USER");
            return new User(user.get().getEmail(),user.get().getPassword(),new ArrayList<>(user.get().getRoles()));
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
/**
 *  if ("javainuse".equals(username)) {//$2a$10$cIMDmrhN2tL7EYL1qIoKfOr.wmvWo.rD6MepPFEiNd73EKvdikyy.  $2a$10$5AAQN7LbruBzmDflL703z.FI8cUj7iUfz/mW3Msqi5FtJVfr8iZo6
 *             return  new User("javainuse","$2a$10$cIMDmrhN2tL7EYL1qIoKfOr.wmvWo.rD6MepPFEiNd73EKvdikyy.",new ArrayList<>());
 *         }
 *        Token  eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE2NDk2NzY0MDd9.cE8GTlETdW_n7ZDPnHpdcXh0HsHljwUuKNfSbI0zL-k
 */