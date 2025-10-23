package poly.edu.ass.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/css/**", "/js/**", "/","profile").permitAll()
                        .requestMatchers("/admin/**").hasRole("Admin")
                        .requestMatchers("/nhanvien/**").hasRole("Nhân viên")
                        .requestMatchers("/khachhang/**").hasRole("khách hàng")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error") // 👈 thêm dòng này để thấy rõ redirect khi sai
                        .permitAll()
                )
                .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll())
                .userDetailsService(userDetailsService);

        return http.build();
    }
}
