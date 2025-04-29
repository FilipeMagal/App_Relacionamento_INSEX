package com.insexba.relacionamento_insex.repository;

import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.entity.UserMatchInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMatchInteractionRepository extends JpaRepository<UserMatchInteraction, Long> {
    List<UserMatchInteraction> findByOriginUser(User originUser);
    Optional<UserMatchInteraction> findByOriginUserAndTargetUser(User originUser, User targetUser);
}
