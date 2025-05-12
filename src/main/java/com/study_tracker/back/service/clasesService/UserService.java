package com.study_tracker.back.service.clasesService;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.userDto.UserEntityResponseDto;
import com.study_tracker.back.entity.UserEntity;
import com.study_tracker.back.exceptions.EntityNotFoundException;
import com.study_tracker.back.mappers.UserMapper;
import com.study_tracker.back.repository.IUserRepository;
import com.study_tracker.back.service.interfacesService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private UserMapper userMapper;
    @Override
    public ApiResponse<UserEntityResponseDto> getUserById(Long id) {
        UserEntity user = iUserRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, UserEntity.class.getSimpleName())
        );
        UserEntityResponseDto userResponse = userMapper.toDto(user);
        return ApiResponse.success(userResponse, "User retrived successfully");
    }
}
