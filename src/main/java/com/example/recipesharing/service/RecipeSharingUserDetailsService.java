package com.example.recipesharing.service;

import com.example.recipesharing.exception.AuthenticationFailureException;
import com.example.recipesharing.model.RecipeCreator;
import com.example.recipesharing.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashSet;
import java.util.Set;

@Service
public class RecipeSharingUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        RecipeCreator user = userRepository.findByUserName(userName);

        if (user != null) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            // hardcoding authority because we don't want to handle Authorization now
            authorities.add(new SimpleGrantedAuthority("USER"));
            return new org.springframework.security.core.userdetails.User(user.getUserName(),
                    user.getPassword(), authorities);
        }else{
            throw new AuthenticationFailureException(RecipeCreator.class, "userName", userName);
        }
    }
}
