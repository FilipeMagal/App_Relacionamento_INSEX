package com.insexba.relacionamento_insex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchedProfileDTO {
    private Integer userId;
    private String firstName;
    private String lastName;
    private int age;
    private String bio;
    private String profession;
    private List<String> commonInterests;
    private double matchPercentage;
}