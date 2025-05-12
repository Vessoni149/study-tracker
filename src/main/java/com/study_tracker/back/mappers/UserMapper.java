package com.study_tracker.back.mappers;

import com.study_tracker.back.dto.userDto.UserEntityResponseDto;
import com.study_tracker.back.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {SubjectMapper.class, SessionMapper.class})
public interface UserMapper {
    UserEntityResponseDto toDto(UserEntity user);
}
