package com.insexba.relacionamento_insex.service;

import com.insexba.relacionamento_insex.dto.RegisterProfileDTO;
import com.insexba.relacionamento_insex.entity.Profile;
import org.springframework.stereotype.Service;

@Service
public interface ProfileService {
    void registerProfile(Profile profile);
    RegisterProfileDTO getProfileByUserId (Integer userId);
}
