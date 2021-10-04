package com.example.security;

import com.example.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //actuator
        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll();

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll() // jwt token 여부는 API Gateway Filter에서 확인
                .anyRequest().authenticated()
            .and()
                .addFilter(this.getAuthenticationFilter())
                .formLogin().loginProcessingUrl("/user-service/login")
            .and()
                .headers().frameOptions().disable(); // /h2-console
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        return new AuthenticationFilter(authenticationManager(), accountService, environment, passwordEncoder);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // input pw <-> db encrypt pw
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
    }
}
