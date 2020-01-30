package com.cloud.cmr.security;

import com.cloud.cmr.security.jwt.JwtAuthenticationFilter;
import com.cloud.cmr.security.jwt.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("tibo").password(passwordEncoder.encode("tibo")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilter(jwtAuthenticationFilter(jwtProperties))
                .authorizeRequests()
//                .mvcMatchers("/board/**").hasAnyRole("MEMBER", "BOARD")
//                .mvcMatchers("/members/**").hasRole("MEMBER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtProperties jwtProperties) throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), jwtProperties);
    }
}
