package com.example.ecommerceBE.Config;

import com.example.ecommerceBE.Repository.UserRepository; // 🛑 Đảm bảo đường dẫn này khớp với project của bạn
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    // 1. Tiêm UserRepository vào để Spring biết cách tìm User trong DB
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)

                // BẮT BUỘC CÓ DÒNG NÀY ĐỂ ĐỌC TOKEN KHI THÊM SẢN PHẨM
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers("/auth/**").permitAll()

                        // GET: Cho phép tất cả mọi người xem sản phẩm
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()

                        // POST/PUT/DELETE: Chỉ ADMIN mới được phép
                        .requestMatchers(HttpMethod.POST, "/api/products").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 2. Gắn AuthenticationProvider mới vào đây
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    // 3. Ép Spring Security tìm User bằng cách gọi xuống Database

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // 1. Tìm User từ Database của bạn
            var user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user trong Database"));

            // 2. Chuyển đổi (Map) sang UserDetails của Spring Security
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(String.valueOf(user.getRole())) // Lấy quyền từ DB (ADMIN hoặc USER)
                    .build();
        };
    }

    // 4. Khai báo AuthenticationProvider dùng cơ chế giải mã Bcrypt và đọc từ DB
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();

        configuration.setAllowedOrigins(java.util.List.of("http://localhost:5173")); // Link Frontend của bạn
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(java.util.Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}