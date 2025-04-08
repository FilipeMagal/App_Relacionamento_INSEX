package com.insexba.relacionamento_insex.service;

import com.insexba.relacionamento_insex.enums.user.Gender;
import com.insexba.relacionamento_insex.enums.user.TypeUser;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;

@Service
public interface UserService {
    void registerUser(String firstName, String lastName, String password, String email, Date birth_Data, Gender gender, TypeUser typeUser);
}

