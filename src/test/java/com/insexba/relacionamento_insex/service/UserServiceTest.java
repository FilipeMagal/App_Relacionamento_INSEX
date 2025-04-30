package com.insexba.relacionamento_insex.service;

import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.enums.user.Gender;
import com.insexba.relacionamento_insex.enums.user.TypeUser;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl service;

    @Mock
    UserRepository userRepository;

    User user;

    @BeforeEach
    public void setUp() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("1990-11-04");
        user = new User("Filipe", "Lima", "123456", "filipi99ff@gmail.com", birthDate, Gender.Masculino, TypeUser.Usuario);
    }



}
