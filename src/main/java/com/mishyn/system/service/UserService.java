package com.mishyn.system.service;

import com.mishyn.system.dto.NewUserDTO;
import com.mishyn.system.entity.UserEntity;

import java.util.List;

public interface UserService {

    boolean userExistsByName(String username);

    UserEntity createNewUser(NewUserDTO newUserDTO);

    List<UserEntity> findAll();

    UserEntity findByUsername(String username);
}
