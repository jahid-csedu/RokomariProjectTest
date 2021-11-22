package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(User user, String roleName) throws Exception {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isPresent()) {
            log.info("Role Found: {}", roleName);
            if(userRepository.findByUserName(user.getUserName()).isPresent()) {
                log.error("User already exists: {}", user.getUserName());
                throw new Exception("User already exists");
            }else {
                Set<Role> roles = new HashSet<>();
                roles.add(role.get());
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                log.info("User creating...");
                return userRepository.save(user);
            }
        }else {
            throw new Exception("Invalid Role Name");
        }
    }
}
