package malo.bloc.tree.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
public class JwtUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("javainuse".equals(username)) {//$2a$10$cIMDmrhN2tL7EYL1qIoKfOr.wmvWo.rD6MepPFEiNd73EKvdikyy.  $2a$10$5AAQN7LbruBzmDflL703z.FI8cUj7iUfz/mW3Msqi5FtJVfr8iZo6
            return  new User("javainuse","$2a$10$cIMDmrhN2tL7EYL1qIoKfOr.wmvWo.rD6MepPFEiNd73EKvdikyy.",new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
