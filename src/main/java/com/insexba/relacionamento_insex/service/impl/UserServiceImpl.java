package com.insexba.relacionamento_insex.service.impl;

import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.enums.user.Gender;
import com.insexba.relacionamento_insex.enums.user.TypeUser;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    // Registrar um novo usuário


    @Override
    public void registerUser(String firstName, String lastName, String password, String email, Date birth_Data, Gender gender, TypeUser typeUser) {
        User user = new User();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Usuário já cadastrado com este email");
        }
        java.sql.Date sqlDate = new java.sql.Date(birth_Data.getTime());
        user.setBirth_Data(sqlDate);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEmail(email);
        user.setGender(gender);
        user.setTypeUser(typeUser);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(user.getBirth_Data());

        userRepository.save(user);
    }
}

