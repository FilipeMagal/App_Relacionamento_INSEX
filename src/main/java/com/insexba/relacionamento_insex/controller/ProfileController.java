package com.insexba.relacionamento_insex.controller;

import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
import com.insexba.relacionamento_insex.entity.Profile;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.ProfileRepository;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/insexba")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;  // Para buscar o usuário por ID


    @PostMapping("/register/profile")
    public ResponseEntity<String> registerProfile(@RequestBody RegisterProfileDTO registerProfileDTO) {

        // Verifica se o userId não é nulo antes de continuar
        if (registerProfileDTO.getUserId() == null) {
            return ResponseEntity.badRequest().body("O userId não pode ser nulo.");
        }

        // Verifica se o usuário com o userId existe
        Optional<User> userOptional = userRepository.findById(registerProfileDTO.getUserId());
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        // Verifica se já existe um perfil para esse usuário
        if (profileRepository.existsByUser(userOptional.get())) {
            return ResponseEntity.badRequest().body("Perfil já existente para o usuário");
        }

        // Cria o objeto Profile e associa o usuário encontrado
        Profile profile = new Profile();
        profile.setUser(userOptional.get());  // Associa o usuário ao perfil
        profile.setEthnicity(registerProfileDTO.getEthnicity());
        profile.setEducation(registerProfileDTO.getEducation());
        profile.setMarital_Status(registerProfileDTO.getMaritalStatus());
        profile.setDesired_Relationship(registerProfileDTO.getDesiredRelationship());
        profile.setBio(registerProfileDTO.getBio());
        profile.setProfession(registerProfileDTO.getProfession());

        // Salva o perfil no banco de dados
        profileService.registerProfile(profile);

        return ResponseEntity.ok("Perfil registrado com sucesso!");
    }


    // Endpoint GET para buscar o perfil de um usuário com base no ID do usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<RegisterProfileDTO> getProfile(@PathVariable Integer userId) {
        RegisterProfileDTO profileDTO = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profileDTO);
    }



}
