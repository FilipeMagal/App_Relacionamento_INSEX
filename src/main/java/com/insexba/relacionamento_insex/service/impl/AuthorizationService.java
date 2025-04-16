package com.insexba.relacionamento_insex.service.impl;

import com.insexba.relacionamento_insex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.insexba.relacionamento_insex.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepository.findByEmail(username);
        if (user == null) throw new UsernameNotFoundException("Usuário não encontrado");
        return user; // Sua entidade já implementa UserDetails
    }


}
