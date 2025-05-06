package com.insexba.relacionamento_insex.service;

import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.impl.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("joao@example.com");
        user.setFirstName("João");
        user.setLastName("Silva");
        user.setPassword("senha123");
        // Outros atributos que a entidade User possa ter
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Mock do comportamento do repositório
        when(userRepository.findByEmail("joao@example.com")).thenReturn(user);

        // Chamada ao serviço
        UserDetails userDetails = authorizationService.loadUserByUsername("joao@example.com");

        // Verifica se o retorno é o esperado
        assertNotNull(userDetails);
        assertEquals("joao@example.com", userDetails.getUsername());
        assertEquals("senha123", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock do repositório retornando null
        when(userRepository.findByEmail("naoexiste@example.com")).thenReturn(null);

        // Verifica se a exceção é lançada
        assertThrows(UsernameNotFoundException.class, () -> {
            authorizationService.loadUserByUsername("naoexiste@example.com");
        });
    }
}
