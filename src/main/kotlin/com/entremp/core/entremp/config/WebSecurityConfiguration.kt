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
        auth
            .userDetailsService(bitnessUserDetailsService)
            .passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity?) {
        if(http != null){
            http.csrf().disable()

            http.authorizeRequests()
                .antMatchers("/seller/*")
        }

        http?.csrf()?.disable()
            ?.authorizeRequests()
                ?.antMatchers("/seller/**")?.authenticated()
                ?.antMatchers("/buyer/**")?.authenticated()
                ?.antMatchers("/resources/**")?.permitAll()

                // TODO Eliminar los denyAll al aplicar los nuevos cambios de flujo
                ?.antMatchers("/register")?.denyAll()
                ?.antMatchers("/confirm")?.denyAll()
                ?.antMatchers("/recover")?.denyAll()
                ?.antMatchers("/reset")?.denyAll()
                ?.antMatchers("/choose")?.denyAll()
                ?.anyRequest()?.permitAll()
            ?.and()
                ?.formLogin()
                    ?.loginPage("/login")
                    ?.defaultSuccessUrl("/buyer/home")
            ?.and()
                ?.logout()
            ?.and()
                ?.httpBasic()
    }
}