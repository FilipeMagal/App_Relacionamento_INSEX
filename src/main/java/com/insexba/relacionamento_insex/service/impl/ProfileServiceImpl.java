package com.insexba.relacionamento_insex.service.impl;

import com.insexba.relacionamento_insex.dto.MatchedProfileDTO;
import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
import com.insexba.relacionamento_insex.entity.Interest;
import com.insexba.relacionamento_insex.entity.Profile;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.ProfileRepository;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public void registerProfile(Profile profile) {
        profileRepository.save(profile);
    }

    // Metodo para converter a entidade Profile em DTO
    public RegisterProfileDTO convertToDTO(Profile profile) {
        User user = profile.getUser();
        RegisterProfileDTO dto = new RegisterProfileDTO();
        dto.setId(profile.getId());

        // Adicionando os dados pessoais
        dto.setFirstName(profile.getUser().getFirstName());
        dto.setLastName(profile.getUser().getLastName());
        dto.setGender(profile.getUser().getGender());

        dto.setEthnicity(profile.getEthnicity());
        dto.setEducation(profile.getEducation());
        dto.setMaritalStatus(profile.getMarital_Status());
        dto.setDesiredRelationship(profile.getDesired_Relationship());
        dto.setBio(profile.getBio());
        dto.setProfession(profile.getProfession());
        dto.setUserId(profile.getUser().getId());
        dto.setAge(user.getAge());

        dto.setInterests(user.getInterests().stream()
                .map(Interest::getName)
                .toList()
        );



        return dto;
    }

    // Metodo para encontrar o perfil com base no ID do usuário
    public RegisterProfileDTO getProfileByUserId(Integer userId) {
        // Buscar usuário e perfil juntos
        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);
        try {
                Profile profile = profileOptional.get();
                return convertToDTO(profile);

        } catch (Exception e) {
            // Se o perfil não for encontrado
            throw new RuntimeException("Perfil não encontrado");
        }
    }

    // Lógica de interesses do aplicativo para o match

    @Override
    public List<MatchedProfileDTO> getDiscoverProfiles(Integer currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Interest> currentUserInterests = currentUser.getInterests();
        List<User> allUsers = userRepository.findAll();

        List<MatchedProfileDTO> matchedProfiles = new ArrayList<>();

        for (User other : allUsers) {
            // Pular o próprio usuário
            if (other.getId().equals(currentUserId)) continue;

            // Filtrar por gênero diferente
            if (other.getGender() == currentUser.getGender()) continue;

            List<Interest> otherInterests = other.getInterests();

            // Verificar se outros interesses não são nulos
            if (otherInterests == null) continue;

            // Encontrar interesses em comum
            List<String> common = otherInterests.stream()
                    .map(Interest::getName)
                    .filter(currentUserInterests.stream().map(Interest::getName).collect(Collectors.toSet())::contains)
                    .collect(Collectors.toList());

            if (!common.isEmpty()) {
                double matchPercent = currentUserInterests.isEmpty() ? 0.0 :
                        ((double) common.size() / currentUserInterests.size()) * 100;

                Profile profile = other.getProfile();
                if (profile != null) {
                    matchedProfiles.add(new MatchedProfileDTO(
                            other.getId(),
                            other.getFirstName(),
                            other.getLastName(),
                            other.getAge(),
                            profile.getBio(),
                            profile.getProfession(),
                            common,
                            matchPercent
                    ));
                }
            }
        }

        matchedProfiles.sort((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()));
        return matchedProfiles;
    }

}
