package com.insexba.relacionamento_insex.service;

import com.insexba.relacionamento_insex.dto.MatchedProfileDTO;
import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
import com.insexba.relacionamento_insex.entity.*;
import com.insexba.relacionamento_insex.enums.profile.Desired_Relationship;
import com.insexba.relacionamento_insex.enums.profile.Education;
import com.insexba.relacionamento_insex.enums.profile.Ethnicity;
import com.insexba.relacionamento_insex.enums.profile.MaritalStatus;
import com.insexba.relacionamento_insex.enums.user.Gender;
import com.insexba.relacionamento_insex.repository.*;
import com.insexba.relacionamento_insex.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InterestRepository interestRepository;
    @Mock
    private UserMatchInteractionRepository interactionRepository;
    @Mock
    private MatchRepository matchRepository;

    @BeforeEach
    void setUp() {
    }

/*
    @Test
    void testCreateProfileFromRequestData() {
        // Dados de entrada simulados
        Map<String, String> inputData = new HashMap<>();
        inputData.put("ethnicity", "Branco");
        inputData.put("education", "Ensino_Superior");
        inputData.put("maritalStatus", "Solteiro");
        inputData.put("desiredRelationship", "Namoro");
        inputData.put("bio", "Sou bonito, gostoso, jogo bola e danço");
        inputData.put("profession", "Putão");
        inputData.put("age", "25");
        inputData.put("interests", "Tecnologia, Viagens, Musica");

        // Mock de dados do usuário
        User user = new User();
        user.setId(1);
        user.setFirstName("João");
        user.setLastName("Silva");
        user.setGender(Gender.Masculino);

        // Criando o perfil com os dados mockados
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setEthnicity(Ethnicity.valueOf(inputData.get("ethnicity")));
        profile.setEducation(Education.valueOf(inputData.get("education")));
        profile.setMarital_Status(MaritalStatus.valueOf(inputData.get("maritalStatus")));
        profile.setDesired_Relationship(Desired_Relationship.valueOf(inputData.get("desiredRelationship")));
        profile.setBio(inputData.get("bio"));
        profile.setProfession(inputData.get("profession"));

        // Simulando que o repositório retorna o perfil do usuário com ID 1
        when(profileRepository.findByUserId(1)).thenReturn(Optional.of(profile));

        // Chamada do serviço para obter o perfil
        RegisterProfileDTO dto = profileService.getProfileByUserId(1);

        // Assegura que os dados são retornados corretamente
        assertEquals("João", dto.getFirstName());
        assertEquals("Branco", dto.getEthnicity());
        assertEquals("Ensino_Superior", dto.getEducation());
        assertEquals("Namoro", dto.getDesiredRelationship());
        assertEquals("Sou bonito, gostoso, jogo bola e danço", dto.getBio());
        assertEquals("Putão", dto.getProfession());
        assertEquals("Silva", dto.getLastName());
        verify(profileRepository, times(1)).findByUserId(1); // Verificar se o repositório foi chamado
    }


    @Test
    void testGetProfileByUserId_Success() {
        User user = new User();
        user.setId(1);
        user.setFirstName("João");
        user.setLastName("Silva");
        user.setGender(Gender.valueOf("Masculino"));

        Interest interest = new Interest();
        interest.setName("Cinema");
        user.setInterests(List.of(interest));

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setEthnicity(Ethnicity.valueOf("Pardo"));
        profile.setEducation(Education.valueOf("Ensino_Superior"));
        profile.setMarital_Status(MaritalStatus.valueOf("Solteiro"));
        profile.setDesired_Relationship(Desired_Relationship.valueOf("Namoro"));
        profile.setBio("Sou divertido e leal");
        profile.setProfession("Engenheiro");

        when(profileRepository.findByUserId(1)).thenReturn(Optional.of(profile));

        RegisterProfileDTO dto = profileService.getProfileByUserId(1);

        assertEquals("João", dto.getFirstName());
        assertEquals("Cinema", dto.getInterests().get(0));
        assertEquals("Silva", dto.getLastName());
        assertEquals("Pardo", dto.getEthnicity());
        assertEquals("Ensino_Superior", dto.getEducation());
        assertEquals("Solteiro", dto.getMaritalStatus());
        assertEquals("Namoro", dto.getDesiredRelationship());
        assertEquals("Sou divertido e leal", dto.getBio());
        assertEquals("Engenheiro", dto.getProfession());

        verify(profileRepository, times(1)).findByUserId(1);
    }
*/

    @Test
    void testRegisterProfile_Success() {
        Profile profile = new Profile();
        profileService.registerProfile(profile);
        verify(profileRepository, times(1)).save(profile);
    }




    @Test
    void testGetProfileByUserId_NotFound() {
        when(profileRepository.findByUserId(1)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            profileService.getProfileByUserId(1);
        });

        assertEquals("Perfil não encontrado", thrown.getMessage());
    }

    @Test
    void testInteractWithProfile_MatchCreated() {
        User origin = new User();
        origin.setId(1);
        origin.setFirstName("Alice");

        User target = new User();
        target.setId(2);
        target.setFirstName("Bob");

        when(userRepository.findById(1)).thenReturn(Optional.of(origin));
        when(userRepository.findById(2)).thenReturn(Optional.of(target));
        when(interactionRepository.findByOriginUserAndTargetUser(origin, target)).thenReturn(Optional.empty());
        when(interactionRepository.findByOriginUserAndTargetUser(target, origin))
                .thenReturn(Optional.of(new UserMatchInteraction(target, origin, true, null)));

        String result = profileService.interactWithProfile(1, 2, true);

        assertEquals("É um match!", result);
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    void testInteractWithProfile_Dislike() {
        User origin = new User();
        origin.setId(1);
        User target = new User();
        target.setId(2);

        when(userRepository.findById(1)).thenReturn(Optional.of(origin));
        when(userRepository.findById(2)).thenReturn(Optional.of(target));
        when(interactionRepository.findByOriginUserAndTargetUser(origin, target)).thenReturn(Optional.empty());

        String result = profileService.interactWithProfile(1, 2, false);

        assertEquals("Dislike registrado!", result);
        verify(interactionRepository, times(1)).save(any(UserMatchInteraction.class));
    }
}
