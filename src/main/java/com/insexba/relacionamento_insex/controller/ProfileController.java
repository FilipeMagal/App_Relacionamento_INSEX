    package com.insexba.relacionamento_insex.controller;

    import com.insexba.relacionamento_insex.dto.MatchedProfileDTO;
    import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
    import com.insexba.relacionamento_insex.entity.Profile;
    import com.insexba.relacionamento_insex.entity.User;
    import com.insexba.relacionamento_insex.repository.ProfileRepository;
    import com.insexba.relacionamento_insex.repository.UserRepository;
    import com.insexba.relacionamento_insex.service.ProfileService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
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



        @GetMapping("/user/{userId}")
        public ResponseEntity<RegisterProfileDTO> getProfile(@PathVariable Integer userId) {

            RegisterProfileDTO profileDTO = profileService.getProfileByUserId(userId);

            if (profileDTO != null) {
                return ResponseEntity.ok(profileDTO);
            }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

        @GetMapping("/user/{userId}/discover")
        public ResponseEntity<List<MatchedProfileDTO>> discoverProfiles(@PathVariable Integer userId) {
            List<MatchedProfileDTO> profiles = profileService.getDiscoverProfiles(userId);

            return ResponseEntity.ok(profiles);


        }


    }




