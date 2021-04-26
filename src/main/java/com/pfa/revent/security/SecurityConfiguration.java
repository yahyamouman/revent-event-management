package com.pfa.revent.security;

import com.pfa.revent.security.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder getPaswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPaswordEncoder());
    }
    
   @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/manage").hasRole("MODERATOR")
                .antMatchers("/create","/events","/eventform").hasAnyRole("EDITOR")
                .antMatchers("/profile").hasAnyRole("VIEWER","EDITOR","MODERATOR")
                .antMatchers("/").permitAll()

                .antMatchers("/browse").permitAll()
                .antMatchers("/contact").permitAll()

                .antMatchers("/user/{userId}","/user/{userId}/**").access("@guard.checkUserId(authentication,#userId)")
                .antMatchers("/event/{eventId}/edit","/event/{eventId}/edit/**","/event/{eventId}/update", "/event/{eventId}/update/**","/event/{eventId}/uploadImage","/event/{eventId}/uploadImage/**").access("@guard.checkEventId(authentication,#eventId)")
                .antMatchers("/event/participations?eventId={eventId}").access("@guard.checkEventId(authentication,#eventId)")
                .and()
                .formLogin();
    }
}
