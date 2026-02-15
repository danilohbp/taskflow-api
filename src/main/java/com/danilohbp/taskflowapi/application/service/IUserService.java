package com.danilohbp.taskflowapi.application.service;

import com.danilohbp.taskflowapi.application.dto.user.CreateUserRequest;
import com.danilohbp.taskflowapi.application.dto.user.UserResponse;

import java.util.List;

public interface IUserService {

    UserResponse create(CreateUserRequest request);

    List<UserResponse> listAll();
    UserResponse getById(Long id);

}
