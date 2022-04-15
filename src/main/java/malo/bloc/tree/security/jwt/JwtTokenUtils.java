package malo.bloc.tree.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
@Component
public class JwtTokenUtils implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 48 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    public  boolean validateToken(String token, UserDetails userDetails){
        return getUserNameFromToken(token).equals(userDetails.getUsername()) && isTokenExpire(token);
    }

    public boolean isTokenExpire(String token){
        Date expirationDate= getExpirationDateFromToken(token);
        return Date.from(Instant.ofEpochSecond(System.currentTimeMillis())).after(expirationDate);
    }

    public  String getUserNameFromToken(String token){
       return  getClaimFromToken(token,Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }

    private Claims getAllClaimsFromToken(String token){
        return  Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
      Claims claims = getAllClaimsFromToken(token);
      return claimsResolver.apply(claims);
    }

    public  String generateToken(HashMap<String,Object> claims,String subject){
        return  Jwts.builder().setClaims(claims).setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY*1000))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }

    public  String generateToken(UserDetails userDetails){
        HashMap<String,Object> claims= new HashMap<>();
        return generateToken(claims,userDetails.getUsername());
    }

}
