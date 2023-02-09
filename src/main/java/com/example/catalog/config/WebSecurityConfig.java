package com.example.catalog.config;

import com.example.catalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/img/**").permitAll()
//                .antMatchers("/", "/css/**", "/img/**", "/registration", "/user/{user}").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .loginPage("/#popup")
                .loginProcessingUrl("/perform-login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/main")
                .and()
                /*при выходе из аккаунта удаляются cookie, делает сессию недействительной (false), после выхода ("/logout") перейти на домашнюю страницу ("/")*/
                .logout().deleteCookies("remove").invalidateHttpSession(false)
                .logoutUrl("/logout").logoutSuccessUrl("/")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());

    }


}
