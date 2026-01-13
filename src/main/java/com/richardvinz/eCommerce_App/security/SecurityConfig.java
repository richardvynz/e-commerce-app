//package com.richardvinz.eCommerce_App.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//
//    @Autowired
//    private AuthEntryPointJwt unauthorizedHandler;
//
//    @Autowired
//    DataSource dataSource;
//
//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(requests -> requests.requestMatchers("/h2-console/**","/api/signin").permitAll()
//                .anyRequest().authenticated());
//        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
////        http.formLogin(Customizer.withDefaults());
////        http.httpBasic(Customizer.withDefaults());
//        http.exceptionHandling(
//                exception->exception.authenticationEntryPoint(unauthorizedHandler));
//        http.csrf(AbstractHttpConfigurer::disable);
//        http.headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
//        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }
//
//    @Bean
//    public CommandLineRunner init(UserDetailsService userDetailsService){
//        return args -> {
//            JdbcUserDetailsManager manager = (JdbcUserDetailsManager) userDetailsService;
//
//            UserDetails user1 = User.withUsername("user1")
//                    .password(passwordEncoder().encode("12345678"))
//                    .roles("USER")
//                    .build();
//            UserDetails admin = User.withUsername("admin")
//                    .password(passwordEncoder().encode("12345678"))
//                    .roles("ADMIN")
//                    .build();
//
//            JdbcUserDetailsManager detailsManager = new JdbcUserDetailsManager(dataSource);
//            detailsManager.createUser(user1);
//            detailsManager.createUser(admin);
//        };
//    }
//
////    @Bean
////    public UserDetailsService userDetailsService(){
////        UserDetails user1 = User.withUsername("user1")
////                .roles("USER")
////                .password(passwordEncoder().encode("12345678"))
////                .build();
////
////        UserDetails admin = User.withUsername("admin")
////                .roles("ADMIN")
////                .password(passwordEncoder().encode("12345678"))
////                .build();
////        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
////        userDetailsManager.createUser(user1);
////        userDetailsManager.createUser(admin);
////        return userDetailsManager;
//////        return new InMemoryUserDetailsManager(user1,admin);
////    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthTokenFilter authTokenFilter(){
//        return new AuthTokenFilter();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authManager) throws Exception {
//       return authManager.getAuthenticationManager();
//    }
//}
