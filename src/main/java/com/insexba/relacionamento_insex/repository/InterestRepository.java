package com.insexba.relacionamento_insex.repository;

import com.insexba.relacionamento_insex.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByName(String name);
}
