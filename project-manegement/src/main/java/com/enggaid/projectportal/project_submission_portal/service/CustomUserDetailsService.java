package com.enggaid.projectportal.project_submission_portal.service;

import com.enggaid.projectportal.project_submission_portal.model.User;
import com.enggaid.projectportal.project_submission_portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user= userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User Not Found With Username: "+username);
        }

        Set<GrantedAuthority>authorities =new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.get().getRole().name()));


        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(),authorities);
    }
}
