package com.mishyn.system.service.impl;

import com.mishyn.system.dto.NewUserDTO;
import com.mishyn.system.entity.UserEntity;
import com.mishyn.system.repository.UserRepository;
import com.mishyn.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity createNewUser(NewUserDTO newUserDTO) {
        UserEntity userEntity = UserEntity.builder()
                .username(newUserDTO.getUsername())
                .password(passwordEncoder.encode(newUserDTO.getPassword()))
                .build();
        return userRepository.save(userEntity);
    }

    public boolean userExistsByName(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
