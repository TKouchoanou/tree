package malo.bloc.tree.controller;

import malo.bloc.tree.security.jwt.JwtRequest;
import malo.bloc.tree.security.jwt.JwtResponse;
import malo.bloc.tree.security.jwt.JwtTokenUtils;
import malo.bloc.tree.security.jwt.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
         authenticate(authenticationRequest);
         UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
         String token = jwtTokenUtil.generateToken(userDetails);
         return  ResponseEntity.ok(new JwtResponse(token));
    }

    public void authenticate(JwtRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
        } catch (DisabledException e) {
        throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
        throw new Exception("INVALID_CREDENTIALS", e);
       }
    }
}
