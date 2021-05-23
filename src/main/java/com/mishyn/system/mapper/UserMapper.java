package com.mishyn.system.mapper;

import com.mishyn.system.dto.UserDTO;
import com.mishyn.system.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface UserMapper {

    UserDTO toCommentDTO(UserEntity userEntity);

}
