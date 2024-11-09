package com.mariaclara.cinevault.services;

import com.mariaclara.cinevault.DTOs.UserDTO;
import com.mariaclara.cinevault.entities.User;
import com.mariaclara.cinevault.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createNewUser(UserDTO user) {
        User newUser = new User(user.name(), user.email(), user.password());
        userRepository.save(newUser);
    }
}
