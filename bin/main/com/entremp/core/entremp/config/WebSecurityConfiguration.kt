package com.entremp.core.entremp.config

import com.entremp.core.entremp.config.service.BitnessUserDetailsService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
        private val bitnessUserDetailsService: BitnessUserDetailsService,
        private val passwordEncoder: PasswordEncoder
): WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(bitnessUserDetailsService)
            .passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity?) {
        if (http != null) {
            http.csrf().disable()
            .authorizeRequests()
//                    .antMatchers("/mail").anonymous()
//                    .antMatchers("/login").anonymous()
//                    .antMatchers("/register").anonymous()
                    //TODO make any requests authenticated by configuration
//                    .anyRequest().authenticated()
                    .anyRequest().anonymous()
                .and()
                    .formLogin()
                .and()
                    .logout()
                .and()
                    .httpBasic()
        }
    }
}