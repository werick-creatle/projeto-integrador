package br.com.gamestore.loja_api.config;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration //@Configuration: Diz ao Spring que esta é uma classe de configuração de segurança.
@EnableWebSecurity //@EnableWebSecurity: Habilita as configurações de segurança web do Spring.
public class SecurityConfig {

    //-------------------------------------------------------------------------------------------------------------------
    //@Bean cria um objeto (um "Bean") que o Spring criar um Bean do tipo PasswordEncoder.
    @Bean
    public PasswordEncoder passwordEncoder() {
        /*
         * BCryptPasswordEncoder é a implementação de hashing de senha
         * mais recomendada e segura que o Spring Security oferece.
         */
        return new BCryptPasswordEncoder();
    }

    //---------------------------------------------------------------------------------------------------------------------
    /*
     * Esse método `securityFilterChain` é basicamente o "segurança" da API.
     * É aqui que eu defini quem pode acessar O QUÊ.
     *
     * O que eu fiz aqui:
     * 1. Desliguei o 'csrf()' e mandei o Spring não criar 'Sessão' (STATELESS),
     * porque eu estou fazendo uma API REST moderna (que vai usar Tokens,
     * não sessão de navegador).
     *
     * 2. Eu liberei (permitAll) as rotas PÚBLICAS:
     * - POST /auth/register -> Para qualquer um poder se CADASTRAR.
     * - GET /api/jogos/** -> Para qualquer um poder VER os jogos
     * (o catálogo, novidades, etc.).
     *
     * 3. Eu tranquei (authenticated) TODO O RESTO (anyRequest):
     * - Isso significa que para cadastrar um jogo novo, deletar, etc.,
     * o usuário vai precisar estar LOGADO.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/login/register").permitAll()

                        .requestMatchers(HttpMethod.POST, "/login/login").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/jogos/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

//-----------------------------------------------------------------------------------------------------------------------
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    // 1. Recebe o 'authenticationConfiguration' do Spring
    // 2. Retorna o 'getAuthenticationManager()' (o "gerente" que queremos)
    return authenticationConfiguration.getAuthenticationManager();
}


}