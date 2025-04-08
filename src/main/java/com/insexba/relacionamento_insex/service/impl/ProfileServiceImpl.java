package com.insexba.relacionamento_insex.service.impl;

import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
import com.insexba.relacionamento_insex.entity.Profile;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.ProfileRepository;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
