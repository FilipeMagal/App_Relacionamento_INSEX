package com.insexba.relacionamento_insex.service;

import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.enums.user.Gender;
import com.insexba.relacionamento_insex.enums.user.TypeUser;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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


    @Test
    public void deveRegistrarUsuarioComSucesso() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("1990-11-04");

        String email = "novo@email.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        service.registerUser("Ana", "Silva", "123456", email, birthDate, Gender.Feminino, TypeUser.Usuario);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User userSalvo = userCaptor.getValue();
        assertEquals("Ana", userSalvo.getFirstName());
        assertEquals("Silva", userSalvo.getLastName());
        assertEquals(email, userSalvo.getEmail());
        assertEquals(Gender.Feminino, userSalvo.getGender());
        assertEquals(TypeUser.Usuario, userSalvo.getTypeUser());
    }

    @Test
    public void deveLancarExcecaoSeEmailJaExistir() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse("1990-11-04");

        String email = "existente@email.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.registerUser("João", "Silva", "senha", email, birthDate, Gender.Masculino, null);
        });

        assertEquals("Usuário já cadastrado com este email", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
