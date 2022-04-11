package malo.bloc.tree.security;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
@Accessors(chain = true)
@Setter
@Getter
@Component
public class JwtRequest {
    private static final long serialVersionUID = 5926468583005150707L;
    private String username;
    private String password;
}
