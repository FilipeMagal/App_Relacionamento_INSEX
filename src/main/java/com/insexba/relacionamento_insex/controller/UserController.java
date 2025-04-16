package com.insexba.relacionamento_insex.controller;

import com.insexba.relacionamento_insex.dto.RegisterUserDTO;
import com.insexba.relacionamento_insex.entity.Interest;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.InterestRepository;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/insexba")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InterestRepository interestRepository;

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
        user.setPassword(registerUserDTO.getPassword());
        user.setGender(registerUserDTO.getGender());
        user.setEmail(registerUserDTO.getEmail());
        user.setTypeUser(registerUserDTO.getTypeUser());
        user.setBirthData(registerUserDTO.getBirthData());


        // Salva o usuário no banco
        userService.registerUser(user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail(), user.getBirthData(), user.getGender(), user.getTypeUser());

        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

    @PostMapping("/{userId}/interests")
    public ResponseEntity<?> addInterests(@PathVariable Integer userId,
                                          @RequestBody Map<String, List<String>> request
                                          ) {
        List<String> interestNames = request.get("interests");

        User user = userRepository.findById(userId).orElseThrow();

        for (String interestName : interestNames) {
            Interest interest = interestRepository.findByName(interestName)
                    .orElseGet(() -> interestRepository.save(new Interest(interestName)));

            if (!user.getInterests().contains(interest)) {
                user.getInterests().add(interest);
            }
        }




        userRepository.save(user);
        return ResponseEntity.ok("Interesses adicionados com sucesso!");
    }





}
