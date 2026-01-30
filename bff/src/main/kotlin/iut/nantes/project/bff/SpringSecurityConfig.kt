package iut.nantes.project.bff

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SpringSecurityConfig {

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            authorizeHttpRequests {
                authorize("/ponies", permitAll)
                authorize(anyRequest, authenticated)
            }
            httpBasic { }
            formLogin { }
        }
        return http.build()
    }
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailService(passwordEncoder: PasswordEncoder): UserDetailsManager {
        val admin = User.withUsername("ADMIN")
            .password(passwordEncoder.encode("ADMIN"))
            .roles("ADMIN")
            .build()
        val demo = User.withUsername("login")
            .password(passwordEncoder.encode("password"))
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(admin, demo)
    }


}