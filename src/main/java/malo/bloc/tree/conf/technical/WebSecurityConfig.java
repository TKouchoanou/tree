package malo.bloc.tree.conf.technical;

import malo.bloc.tree.security.JwtAuthenticationEntryPoint;
import malo.bloc.tree.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    /**
     configure le gestionnaire d'authentification pour savoir où charger l'utilisateur
     afin de comparer les identifiants (username,password) et utilise l'encodeur BCryptPasswordEncoder
     pour la comparaison de mot de pass
     **/
     @Autowired
     public void configureGlobal(AuthenticationManagerBuilder managerBuilder) throws Exception {
     managerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
         return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         // on desactive csrf car on en a pas besoin  3.589,11
        http.csrf().disable()
                .authorizeRequests().antMatchers("/authenticate").permitAll()           // authorise toutes les requêtes suivantes sans authentification
                .anyRequest().authenticated().and()                                               //toutes les autres requêtes ont besoin d'authentification
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()            //Gère le requêtes necessitant une authentification et non authentifié par ce point d'entrée
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);                      // assure toi qu c'est la politique de session sans état qui est utilisé, on pas besoin de stocker les informations utilisateurs

        http.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);     //ajoute un filtre pour valider le token de toutes requêtes
    }
}
