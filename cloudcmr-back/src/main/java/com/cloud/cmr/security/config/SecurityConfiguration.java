package com.cloud.cmr.security.config;

import com.cloud.cmr.security.web.JwtAuthenticationFilter;
import com.cloud.cmr.security.web.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private JwtProperties jwtProperties;
    private ObjectMapper mapper;

    public SecurityConfiguration(JwtProperties jwtProperties, ObjectMapper mapper) {
        super();
        this.jwtProperties = jwtProperties;
        this.mapper = mapper;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder.encode("user")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .addFilter(jwtAuthenticationFilter())
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), jwtProperties, mapper);
    }
}
