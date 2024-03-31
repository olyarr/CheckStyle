package com.example.Warehouse.service;

import com.example.Warehouse.model.Enums.Role;
import com.example.Warehouse.model.User;
import com.example.Warehouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                )).orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }

    public boolean createUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()) return false;
        user.setPassword("{noop}" + user.getPassword());
        user.setRole(Role.USER);
        userRepository.save(user);
        return true;
    }

    public Integer getCountUsers(){
        return userRepository.getCountUsers();
    }

    public List<User> getUsers() { return (List<User>) userRepository.findAll(); }
}
