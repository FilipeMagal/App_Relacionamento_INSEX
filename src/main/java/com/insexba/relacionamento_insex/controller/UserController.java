package com.insexba.relacionamento_insex.controller;

import com.insexba.relacionamento_insex.dto.RegisterDTO;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insexba")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO) {
            // Verifica se o email já está registrado
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Usuário já existe com o email informado");
        }

        // Cria o objeto Usuario e converte os dados do DTO
        User user = new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setPassword(registerDTO.getPassword()); // Supondo que você tenha esse campo no User
        user.setAge(registerDTO.getAge());
        user.setEthnicity(registerDTO.getEthnicity());
        user.setGender(registerDTO.getGender());
        user.setBio(registerDTO.getBio());
        user.setEmail(registerDTO.getEmail());
        user.setTypeUser(registerDTO.getTypeUser()); // Supondo que você tenha esse campo no User

        // Salva o usuário no banco
        userService.registerUser(user);

        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }
}
