package com.insexba.relacionamento_insex.service.impl;

import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    // Registrar um novo usuário

    @Override
    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Usuário já cadastrado com este email");
        }
        userRepository.save(user);
    }
}

