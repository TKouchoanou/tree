package malo.bloc.tree.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class JwtResponse {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
}
