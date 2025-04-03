package com.insexba.relacionamento_insex.service;

import com.insexba.relacionamento_insex.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void registerUser(User user);
}
