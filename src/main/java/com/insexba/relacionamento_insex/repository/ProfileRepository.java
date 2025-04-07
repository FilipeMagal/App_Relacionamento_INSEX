package com.insexba.relacionamento_insex.repository;

import com.insexba.relacionamento_insex.entity.Profile;
import com.insexba.relacionamento_insex.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    boolean existsByUser(User user);
    Optional<Profile> findByUserId(Integer userId);
}
