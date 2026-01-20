package com.richardvinz.eCommerce_App.security.controller;

import com.richardvinz.eCommerce_App.security.JwtUtils;
import com.richardvinz.eCommerce_App.security.dto.request.LoginRequest;
import com.richardvinz.eCommerce_App.security.dto.request.SignUpRequest;
import com.richardvinz.eCommerce_App.security.dto.response.MessageResponse;
import com.richardvinz.eCommerce_App.security.dto.response.UserInfoResponse;
import com.richardvinz.eCommerce_App.security.service.UserDetailsImpl;
import com.richardvinz.eCommerce_App.user.models.Role;
import com.richardvinz.eCommerce_App.user.models.User;
import com.richardvinz.eCommerce_App.user.repository.RoleRepository;
import com.richardvinz.eCommerce_App.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.richardvinz.eCommerce_App.user.enums.AppRole.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request){
        Authentication authentication;

        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
            );
        } catch(AuthenticationException exception){
            Map<String,Object> map = new HashMap<>();
            map.put("message","Bad Credential");
            map.put("status",false);

            return new ResponseEntity<>(map,NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJWTCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        UserInfoResponse  response = new UserInfoResponse(userDetails.getId(), jwtCookie.toString(),userDetails.getUsername(), roles);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,jwtCookie.toString())
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity
                    .badRequest().body("Error: Username is already taken");
        }
        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity
                    .badRequest().body("Error: Email is already taken");
        }

        User user = new User(request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            Role userRole = roleRepository.findByRoleName(ROLE_USER)
                    .orElseThrow(()-> new RuntimeException("Error: role is not found!"));
            roles.add(userRole);
        } else{
            strRoles.forEach(role->{
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(ROLE_ADMIN)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found!"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(ROLE_SELLER)
                                .orElseThrow(()-> new RuntimeException("Error: Role is not found!"));
                        roles.add(sellerRole);
                        break;
                    default:
                }
            });
        }
                user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

        @GetMapping("/username")
    public String getCurrentUsername(Authentication authentication){
        if(authentication != null){
            return authentication.getName();
        }
        return "Signed out";
    }

        @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(),roles);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?>signOutUser(){
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new MessageResponse("You are now Signed out!"));
    }
}
