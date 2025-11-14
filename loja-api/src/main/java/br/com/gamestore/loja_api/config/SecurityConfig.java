package br.com.gamestore.loja_api.config;

import br.com.gamestore.loja_api.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        /*
         * BCryptPasswordEncoder é a implementação de hashing de senha
         * mais recomendada e segura que o Spring Security oferece.
         */
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())

                //aplicaçõa de metodo gloal Cors para correção de bug
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/jogos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/jogos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/jogos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/jogos/**").hasRole("ADMIN")
                        .requestMatchers("/api/carrinho/**").hasRole("USER")
                        .requestMatchers("/api/pedidos/**").hasRole("USER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Metodo para a correção Bug na API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite requisições de qualquer origem
        configuration.setAllowedOrigins(Arrays.asList("*"));
        // Permite os métodos (GET, POST, PUT, DELETE)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        // Permite todos os headers (incluindo "Authorization" para o token)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a TODAS as rotas ("/**")
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
