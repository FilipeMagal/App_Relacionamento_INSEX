package com.insexba.relacionamento_insex.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserMatchInteraction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User originUser;

    @ManyToOne
    private User targetUser;

    private Boolean liked; // true = like, false = dislike

    private LocalDateTime interactionTime;

    public UserMatchInteraction(User originUser, User targetUser, Boolean liked, LocalDateTime interactionTime) {
        this.originUser = originUser;
        this.targetUser = targetUser;
        this.liked = liked;
        this.interactionTime = interactionTime;
    }
}
