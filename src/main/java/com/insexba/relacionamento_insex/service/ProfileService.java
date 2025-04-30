package com.insexba.relacionamento_insex.service;

import com.insexba.relacionamento_insex.dto.MatchedProfileDTO;
import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
import com.insexba.relacionamento_insex.entity.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProfileService {
    void registerProfile(Profile profile);
    RegisterProfileDTO getProfileByUserId (Integer userId);
    public List<MatchedProfileDTO> getDiscoverProfiles(Integer userId);
    void changeProfile(Integer userId, RegisterProfileDTO registerProfileDTO);
    String interactWithProfile(Integer originUserId, Integer targetUserId, boolean liked);

}
