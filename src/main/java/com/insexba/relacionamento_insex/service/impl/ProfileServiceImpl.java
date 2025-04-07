package com.insexba.relacionamento_insex.service.impl;

import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
import com.insexba.relacionamento_insex.entity.Profile;
import com.insexba.relacionamento_insex.repository.ProfileRepository;
import com.insexba.relacionamento_insex.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Override
    public void registerProfile(Profile profile) {
        profileRepository.save(profile);
    }

    // Metodo para encontrar o perfil do usuário com base no ID do usuário
    public RegisterProfileDTO getProfileByUserId(Integer userId) {
        Optional<Profile> profileOptional = profileRepository.findByUserId(userId);

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            return convertToDTO(profile);
        } else {
            throw new RuntimeException("Perfil não encontrado para o usuário com ID: " + userId);
        }
    }

    // Metodo para converter a entidade Profile em DTO
    public RegisterProfileDTO convertToDTO(Profile profile) {
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



        return dto;
    }
}
