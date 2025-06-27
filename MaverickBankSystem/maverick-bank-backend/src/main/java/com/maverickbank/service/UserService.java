package com.maverickbank.service;

import com.maverickbank.entity.Role;
import com.maverickbank.entity.User;
import com.maverickbank.repository.RoleRepository;
import com.maverickbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerCustomer(User user) {
        // Save password as plain text (no encoding)
        user.setPassword(user.getPassword());
        Role customerRole = roleRepository.findByName("CUSTOMER").orElseThrow();
        user.setRoles(Collections.singleton(customerRole));
        return userRepository.save(user);
    }
}
