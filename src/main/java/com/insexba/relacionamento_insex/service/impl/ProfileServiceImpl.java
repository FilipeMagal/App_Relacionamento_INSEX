package com.insexba.relacionamento_insex.service.impl;

import com.insexba.relacionamento_insex.dto.MatchedProfileDTO;
import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
import com.insexba.relacionamento_insex.entity.*;
import com.insexba.relacionamento_insex.repository.*;
import com.insexba.relacionamento_insex.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.postgresql.core.JavaVersion.other;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    UserMatchInteractionRepository interactionRepository;
    @Autowired
    MatchRepository matchRepository;


    @Override
    public void registerProfile(Profile profile) {
        profileRepository.save(profile);
    }

    // Metodo para converter a entidade Profile em DTO
    public RegisterProfileDTO convertToDTO(Profile profile) {
        User user = profile.getUser();
        RegisterProfileDTO dto = new RegisterProfileDTO();
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

    // Lógica de interesses do aplicativo para o match
    @Override
    public List<MatchedProfileDTO> getDiscoverProfiles(Integer currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<UserMatchInteraction> interactions = interactionRepository.findByOriginUser(currentUser);
        List<Integer> alreadyInteractedUserIds = interactions.stream()
                .map(interaction -> interaction.getTargetUser().getId())
                .collect(Collectors.toList());

        List<Interest> currentUserInterests = currentUser.getInterests();
        List<User> allUsers = userRepository.findAll();

        List<MatchedProfileDTO> matchedProfiles = new ArrayList<>();

        for (User other : allUsers) {
            // Pular o próprio usuário
            if (other.getId().equals(currentUserId)) continue;
            if (alreadyInteractedUserIds.contains(other.getId())) continue;

            // Filtrar por gênero diferente
            if (other.getGender() == currentUser.getGender()) continue;

            // Obter os interesses do outro usuário
            List<Interest> otherInterests = other.getInterests();  // Aqui você define a lista de interesses do outro usuário
            if (otherInterests == null || otherInterests.isEmpty()) continue;  // Verifica se os interesses do outro usuário são nulos ou vazios

            // Encontrar interesses em comum
            List<String> common = otherInterests.stream()
                    .map(Interest::getName)
                    .filter(currentUserInterests.stream().map(Interest::getName).collect(Collectors.toSet())::contains)
                    .collect(Collectors.toList());

            if (!common.isEmpty()) {
                double matchPercent = currentUserInterests.isEmpty() ? 0.0 :
                        ((double) common.size() / currentUserInterests.size()) * 100;

                if (otherInterests == null || otherInterests.isEmpty()) continue;  // Já existe
                if (currentUserInterests == null || currentUserInterests.isEmpty()) continue;  // Garantir que o usuário atual tenha interesses

                Profile profile = other.getProfile();

                String base64Image = null;
                if (profile.getProfilePicture() != null) {
                    base64Image = Base64.getEncoder().encodeToString(profile.getProfilePicture());
                }

                if (profile != null) {
                    matchedProfiles.add(new MatchedProfileDTO(
                            other.getId(),
                            other.getFirstName(),
                            other.getLastName(),
                            other.getAge(),
                            profile.getBio(),
                            profile.getProfession(),
                            common,
                            matchPercent,
                            base64Image
                    ));
                }
            }
        }

        matchedProfiles.sort((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()));
        return matchedProfiles;
    }


    @Override
    public void changeProfile(Integer userId, RegisterProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        profile.setEthnicity(dto.getEthnicity());
        profile.setEducation(dto.getEducation());
        profile.setMarital_Status(dto.getMaritalStatus());
        profile.setDesired_Relationship(dto.getDesiredRelationship());
        profile.setBio(dto.getBio());
        profile.setProfession(dto.getProfession());

        try {
            MultipartFile file = dto.getProfilePicture();
            if (file != null && !file.isEmpty()) {
                profile.setProfilePicture(file.getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar imagem");
        }

        if (dto.getInterests() != null) {
            List<Interest> updatedInterests = dto.getInterests().stream()
                    .map(name -> interestRepository.findByName(name)
                            .orElseGet(() -> {
                                Interest newInterest = new Interest();
                                newInterest.setName(name);
                                return interestRepository.save(newInterest);
                            })
                    )
                    .collect(Collectors.toList());

            user.setInterests(updatedInterests);
        }

        profileRepository.save(profile);
        userRepository.save(user);
    }

    public String interactWithProfile(Integer originUserId, Integer targetUserId, boolean liked) {
        if (originUserId.equals(targetUserId)) {
            throw new RuntimeException("Você não pode interagir com seu próprio perfil.");
        }

        User originUser = userRepository.findById(originUserId)
                .orElseThrow(() -> new RuntimeException("Usuário de origem não encontrado"));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Usuário de destino não encontrado"));

        // Verificar se já existe interação
        Optional<UserMatchInteraction> existing = interactionRepository.findByOriginUserAndTargetUser(originUser, targetUser);
        if (existing.isPresent()) {
            return "Você já interagiu com esse usuário.";
        }

        // Criar nova interação
        UserMatchInteraction interaction = new UserMatchInteraction();
        interaction.setOriginUser(originUser);
        interaction.setTargetUser(targetUser);
        interaction.setLiked(liked);
        interaction.setInteractionTime(java.time.LocalDateTime.now());
        interactionRepository.save(interaction);

        // Se foi um like, verificar reciprocidade
        if (liked) {
            Optional<UserMatchInteraction> reciprocal = interactionRepository.findByOriginUserAndTargetUser(targetUser, originUser);
            if (reciprocal.isPresent() && Boolean.TRUE.equals(reciprocal.get().getLiked())) {
                // Criar match
                Match match = new Match();
                match.setUser1(originUser);
                match.setUser2(targetUser);
                match.setMatchedAt(java.time.LocalDateTime.now());
                matchRepository.save(match);

                return "É um match!";
            }
        }

        return liked ? "Like registrado!" : "Dislike registrado!";
    }

    public List<MatchedProfileDTO> getUserMatches(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Match> matches = matchRepository.findByUser1OrUser2(user, user);

        return matches.stream().map(match -> {
            User matchedUser = match.getUser1().equals(user) ? match.getUser2() : match.getUser1();
            Profile profile = matchedUser.getProfile();
            String base64Image = profile.getProfilePicture() != null ?
                    Base64.getEncoder().encodeToString(profile.getProfilePicture()) : null;

            return new MatchedProfileDTO(
                    matchedUser.getId(),
                    matchedUser.getFirstName(),
                    matchedUser.getLastName(),
                    matchedUser.getAge(),
                    profile.getBio(),
                    profile.getProfession(),
                    matchedUser.getInterests().stream().map(Interest::getName).collect(Collectors.toList()),
                    100.0,
                    base64Image
            );
        }).toList();
    }


}
