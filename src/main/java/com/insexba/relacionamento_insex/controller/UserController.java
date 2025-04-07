package com.insexba.relacionamento_insex.controller;

import com.insexba.relacionamento_insex.dto.RegisterUserDTO;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/insexba")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserDTO registerUserDTO) {
            // Verifica se o email já está registrado
        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Usuário já existe com o email informado");
        }

        // Cria o objeto Usuario e converte os dados do DTO
        User user = new User();
        user.setFirstName(registerUserDTO.getFirstName());
        user.setLastName(registerUserDTO.getLastName());
        user.setPassword(registerUserDTO.getPassword()); // Supondo que você tenha esse campo no User
        user.setGender(registerUserDTO.getGender());
        user.setEmail(registerUserDTO.getEmail());
        user.setTypeUser(registerUserDTO.getTypeUser()); // Supondo que você tenha esse campo no User

        String birthDataString = registerUserDTO.getBirth_Data();
        if (birthDataString != null && !birthDataString.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato esperado da string
                Date birthData = sdf.parse(birthDataString); // Convertendo para Date
                user.setBirth_Data(birthData); // Passando o Date para setBirth_Data
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Formato de data inválido");
            }
        }

        // Salva o usuário no banco
        userService.registerUser(user);

        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }




}
