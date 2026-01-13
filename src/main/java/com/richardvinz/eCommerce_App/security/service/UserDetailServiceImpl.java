package com.richardvinz.eCommerce_App.security.service;

import com.richardvinz.eCommerce_App.user.models.User;
import com.richardvinz.eCommerce_App.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND WITH USERNAME: "+ username));
        return UserDetailsImpl.build(user);
    }
}
