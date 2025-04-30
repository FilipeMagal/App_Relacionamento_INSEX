    package com.insexba.relacionamento_insex.controller;

    import com.insexba.relacionamento_insex.dto.MatchedProfileDTO;
    import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
    import com.insexba.relacionamento_insex.entity.Interest;
    import com.insexba.relacionamento_insex.entity.Profile;
    import com.insexba.relacionamento_insex.entity.User;
    import com.insexba.relacionamento_insex.enums.user.TypeUser;
    import com.insexba.relacionamento_insex.repository.ProfileRepository;
    import com.insexba.relacionamento_insex.repository.UserRepository;
    import com.insexba.relacionamento_insex.service.ProfileService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @RestController
    @RequestMapping("/insexba")
    public class ProfileController {

        @Autowired
        private ProfileRepository profileRepository;

        @Autowired
        private ProfileService profileService;

        @Autowired
        private UserRepository userRepository;  // Para buscar o usuário por ID


        // Mostrar o perfil

        @GetMapping("/user/{userId}")
        public ResponseEntity<RegisterProfileDTO> getProfile(@PathVariable Integer userId) {

            RegisterProfileDTO profileDTO = profileService.getProfileByUserId(userId);

            if (profileDTO != null) {
                return ResponseEntity.ok(profileDTO);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Retornar uma lista de perfis que têm interesses em comum com o usuário informado

        @GetMapping("/user/discover")
        public ResponseEntity<List<MatchedProfileDTO>> discoverProfiles(@AuthenticationPrincipal User user) {
            List<MatchedProfileDTO> profiles = profileService.getDiscoverProfiles(user.getId());
            return ResponseEntity.ok(profiles);
        }

        // Registra um perfil vinculado a um usuário

        @PostMapping(value = "/register/profile", consumes = "multipart/form-data")
        public ResponseEntity<String> registerProfile(@ModelAttribute RegisterProfileDTO registerProfileDTO,
                                                      @AuthenticationPrincipal User user) {


            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }

            if (profileRepository.existsByUser(user)) {
                return ResponseEntity.badRequest().body("Perfil já existente para o usuário");
            }

            Profile profile = new Profile();
            profile.setUser(user);
            profile.setEthnicity(registerProfileDTO.getEthnicity());
            profile.setEducation(registerProfileDTO.getEducation());
            profile.setMarital_Status(registerProfileDTO.getMaritalStatus());
            profile.setDesired_Relationship(registerProfileDTO.getDesiredRelationship());
            profile.setBio(registerProfileDTO.getBio());
            profile.setProfession(registerProfileDTO.getProfession());

            try {
                MultipartFile file = registerProfileDTO.getProfilePicture();
                if (file.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
                }
                byte[] profilePictureBytes = file.getBytes();
                System.out.println("Tamanho do array de bytes da imagem (registro): " + profilePictureBytes.length);
                profile.setProfilePicture(profilePictureBytes);
                profileService.registerProfile(profile);


            } catch (Exception e) {
                System.err.println("Erro ao processar imagem: " + e.getMessage()); // Imprime a mensagem da exceção
                e.printStackTrace(); // Imprime o rastreamento da pilha para mais detalhes
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar imagem");
            }
            return ResponseEntity.ok("Perfil registrado com sucesso!");
        }

        // Altera os dados do perfil do usuário

        @PutMapping(value = "/change/profile", consumes = "multipart/form-data")
        public ResponseEntity<String> changeProfile(@ModelAttribute RegisterProfileDTO registerProfileDTO,
                                                    @AuthenticationPrincipal User user){

            try {
                profileService.changeProfile(user.getId(), registerProfileDTO);
                return ResponseEntity.ok("Perfil atualizado com sucesso!");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }

        @PostMapping("/interact/{targetUserId}")
        public ResponseEntity<String> interactWithUser(
                @AuthenticationPrincipal User currentUser, // ou pegue o ID via token
                @PathVariable Integer targetUserId,
                @RequestParam boolean liked) {

            try {
                String message = profileService.interactWithProfile(currentUser.getId(), targetUserId, liked);
                return ResponseEntity.ok(message);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }


    }




