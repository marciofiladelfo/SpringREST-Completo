package br.com.alura.forumproject.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity //Habilita o spring security
@Configuration
@Profile("dev")
public class DevSecurityConfigurations extends WebSecurityConfigurerAdapter {

    /**
     * Configurações de Autorização
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* Efetua as autorizações de acesso nas URLs */
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().csrf().disable();

    }

}
