package com.insexba.relacionamento_insex.controller;

import com.insexba.relacionamento_insex.dto.RegisterUserDTO;
import com.insexba.relacionamento_insex.entity.Interest;
import com.insexba.relacionamento_insex.entity.User;
import com.insexba.relacionamento_insex.repository.InterestRepository;
import com.insexba.relacionamento_insex.repository.UserRepository;
import com.insexba.relacionamento_insex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/insexba")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    InterestRepository interestRepository;


    @PostMapping("/{userId}/interests")
    public ResponseEntity<?> addInterests(@PathVariable Integer userId,
                                          @RequestBody Map<String, List<String>> request
                                          ) {
        List<String> interestNames = request.get("interests");

        User user = userRepository.findById(userId).orElseThrow();

        for (String interestName : interestNames) {
            Interest interest = interestRepository.findByName(interestName)
                    .orElseGet(() -> interestRepository.save(new Interest(interestName)));

            if (!user.getInterests().contains(interest)) {
                user.getInterests().add(interest);
            }
        }

        userRepository.save(user);
        return ResponseEntity.ok("Interesses adicionados com sucesso!");
    }





}
