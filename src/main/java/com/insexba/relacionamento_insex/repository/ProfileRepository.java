package com.insexba.relacionamento_insex.repository;

import com.insexba.relacionamento_insex.entity.Profile;
import com.insexba.relacionamento_insex.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    @Query("SELECT p FROM Profile p JOIN FETCH p.user u WHERE u.id = :userId")
    Optional<Profile> findByUserId(@Param("userId") Integer userId);
    boolean existsByUser(User user);
}
