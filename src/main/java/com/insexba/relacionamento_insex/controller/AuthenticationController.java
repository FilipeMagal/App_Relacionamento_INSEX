package com.insexba.relacionamento_insex.controller;

import com.insexba.relacionamento_insex.dto.AuthenticationDTO;
import com.insexba.relacionamento_insex.dto.LoginResponseDTO;
import com.insexba.relacionamento_insex.dto.RegisterDTO;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.infra.security.TokenService;
import com.insexba.relacionamento_insex.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO dados) {
        System.out.println("Dados de Login: " + dados);

        try {
            var loginPassword = new UsernamePasswordAuthenticationToken(dados.email(), dados.password());
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dados.email(), dados.password()));

            User user = (User) auth.getPrincipal();

            var token = tokenService.generateToken((User)
                    auth.getPrincipal()); // Geração do token JWT
            return ResponseEntity.ok(new LoginResponseDTO(token)); // Retorna o token gerado
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Erro na autenticação: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO dados){
        if (this.userRepository.findByEmail(dados.email()) != null) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("message", "Usuário já cadastrado com o email"));
        }
        String senhaEncriptada = new BCryptPasswordEncoder().encode(dados.password());

        // Criar novo usuário
        User newUser = new User(
                dados.firstName(),
                dados.lastName(),
                senhaEncriptada,
                dados.email(),
                dados.birthData(),
                dados.gender(),
                dados.user()
        );

        this.userRepository.save(newUser);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }
}
