package org.example.service;

import org.example.model.User;
import org.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userRepo.save(new User("admin", "admin"));
        if (userRepo.findByUsername(username) == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return new User("admin", "admin");
    }
}
