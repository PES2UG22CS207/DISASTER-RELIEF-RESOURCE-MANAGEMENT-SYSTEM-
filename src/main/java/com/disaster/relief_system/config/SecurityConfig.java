package com.disaster.relief_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Supports {bcrypt} (new) and also raw legacy passwords (treated as {noop}).
        PasswordEncoder delegating = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return delegating.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (encodedPassword == null) {
                    return false;
                }
                String candidate = encodedPassword;
                if (!encodedPassword.startsWith("{")) {
                    candidate = "{noop}" + encodedPassword;
                }
                return delegating.matches(rawPassword, candidate);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LoginSuccessHandler successHandler) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error", "/style.css", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/users/register").permitAll()
                        .requestMatchers("/api/auth/current-user").permitAll()

                        .requestMatchers("/admin-dashboard", "/users", "/camp-page", "/reports").hasRole("ADMIN")
                        .requestMatchers("/camp-dashboard").hasRole("CAMP_MANAGER")
                        .requestMatchers("/warehouse-dashboard", "/inventory", "/warehouses").hasAnyRole("WAREHOUSE_MANAGER", "ADMIN")
                        .requestMatchers("/logistics-dashboard", "/allocations", "/deliveries").hasAnyRole("LOGISTICS_OFFICER", "ADMIN")

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/camps/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/requests/approve/**", "/api/requests/reject/**").hasRole("ADMIN")
                        .requestMatchers("/api/requests/manager/**", "/api/camps/manager/**").hasRole("CAMP_MANAGER")

                        .requestMatchers(HttpMethod.POST, "/api/warehouses/**", "/api/inventory/**").hasAnyRole("WAREHOUSE_MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/inventory/**").hasAnyRole("WAREHOUSE_MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/warehouses/**").hasAnyRole("WAREHOUSE_MANAGER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/resources/**").hasAnyRole("WAREHOUSE_MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/resources/**").hasAnyRole("WAREHOUSE_MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/resources/**").hasAnyRole("WAREHOUSE_MANAGER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/deliveries/**", "/api/allocations/auto/**").hasAnyRole("LOGISTICS_OFFICER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/deliveries/**").hasAnyRole("LOGISTICS_OFFICER", "ADMIN")

                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}