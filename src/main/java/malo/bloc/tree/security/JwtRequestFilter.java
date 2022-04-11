package malo.bloc.tree.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Log4j2
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
          String jwtToken=getTokenFromRequest(request);
          String username=null;
          if(jwtToken!=null){
              try {
                  username= jwtTokenUtils.getUserNameFromToken(jwtToken);
              }catch (ExpiredJwtException e){
                  log.error(" jwt token has expired");
              }
          }

          if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
              UserDetails userDetails = userDetailsService.loadUserByUsername(username);
              if(jwtTokenUtils.validateToken(jwtToken,userDetails)){
                  UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                  authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authenticationToken);
              }
          }
          filterChain.doFilter(request,response);

    }

    protected String getTokenFromRequest(HttpServletRequest request){
      String requestHeader=  request.getHeader("Authorization");
      if(requestHeader==null)
          return null;
      if(requestHeader.startsWith("Bearer "))
          return requestHeader.substring(7);
      return null;
    }
}
