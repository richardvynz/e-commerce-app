package com.richardvinz.eCommerce_App.security.config;

import com.richardvinz.eCommerce_App.security.AuthEntryPointJwt;
import com.richardvinz.eCommerce_App.security.AuthTokenFilter;
import com.richardvinz.eCommerce_App.security.service.UserDetailServiceImpl;
import com.richardvinz.eCommerce_App.user.enums.AppRole;
import com.richardvinz.eCommerce_App.user.models.Role;
import com.richardvinz.eCommerce_App.user.models.User;
import com.richardvinz.eCommerce_App.user.repository.RoleRepository;
import com.richardvinz.eCommerce_App.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] WHITELIST = {"/h2-console/**",
            "/v3/api-doc/**", "/swagger-ui/", "/api/public/**","/api/register/**","/api/signin/**",
            "api/admin/**", "/api/test/**", "/images/**"};

    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .headers(header -> header.frameOptions(frameOption -> frameOption.sameOrigin()))
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(
                "/v2/api-doc",
                "/configuration/ui",
                "/swagger-resources/**",
                "configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        ));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
//            Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(()->{
                        Role newUser = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUser);
                    });
            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(()->{
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });
            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(()->{
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role>userRoles = Set.of(userRole);
            Set<Role>sellerRoles = Set.of(sellerRole);
            Set<Role>adminRoles = Set.of(adminRole,sellerRole,userRole);

//            Create User if not already present

            if(!userRepository.existsByUsername("user1")){
                User user1 = new User("user1","user1@example.com", passwordEncoder().encode("password1"));
                userRepository.save(user1);
            }
            if(!userRepository.existsByUsername("seller1")){
                User seller1 = new User("seller1","seller@example.com", passwordEncoder().encode("password2"));
                userRepository.save(seller1);
            }
            if(!userRepository.existsByUsername("admin")){
                User admin = new User("admin","admin@example.com", passwordEncoder.encode("password3"));
                userRepository.save(admin);

            }
//                update Roles for existing Users
                userRepository.findByUsername("user1")
                        .ifPresent(user -> {
                            user.setRoles(userRoles);
                            userRepository.save(user);
                        });

            userRepository.findByUsername("seller1")
                    .ifPresent(seller -> {
                        seller.setRoles(sellerRoles);
                        userRepository.save(seller);
                    });
            userRepository.findByUsername("admin")
                    .ifPresent(admin->{
                        admin.setRoles(adminRoles);
                        userRepository.save(admin);
                    });
        };
    }
}