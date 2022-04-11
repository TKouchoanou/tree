package malo.bloc.tree.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
@Getter
@AllArgsConstructor
public class JwtResponse {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
}
