package com.insexba.relacionamento_insex.controller;

import com.insexba.relacionamento_insex.dto.RegisterUserDTO;
import com.insexba.relacionamento_insex.entity.Interest;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.InterestRepository;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    @PostMapping("/interests")
    public ResponseEntity<?> addInterests(@RequestBody Map<String, List<String>> request,
                                          @AuthenticationPrincipal User authenticatedUser) {

        // Verifica se a lista de interesses foi fornecida na requisição
        List<String> interestNames = request.get("interests");

        if (interestNames == null || interestNames.isEmpty()) {
            return ResponseEntity.badRequest().body("Lista de interesses não fornecida ou vazia.");
        }

        // Obtém o usuário autenticado diretamente
        User user = authenticatedUser;

        // Inicializa explicitamente a coleção de interesses
        Hibernate.initialize(user.getInterests());

        // Itera sobre os interesses fornecidos
        for (String interestName : interestNames) {
            // Verifica se o interesse já existe no banco de dados, caso contrário, cria um novo
            Interest interest = interestRepository.findByName(interestName)
                    .orElseGet(() -> interestRepository.save(new Interest(interestName)));

            // Adiciona o interesse ao usuário, caso ainda não tenha sido adicionado
            if (!user.getInterests().contains(interest)) {
                user.getInterests().add(interest);
            }
        }

        // Atualiza o usuário com os interesses adicionados
        userRepository.save(user);

        // Retorna uma resposta de sucesso
        return ResponseEntity.ok("Interesses adicionados com sucesso!");
    }




}
