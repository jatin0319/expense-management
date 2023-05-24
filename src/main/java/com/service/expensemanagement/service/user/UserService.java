package com.service.expensemanagement.service.user;

import com.service.expensemanagement.dto.*;

import java.util.List;

public interface UserService {
    CreateGroupResponseDto createGroupDetails(CreateGroupRequestDto createGroupRequest, String operation);

    CreateUserResponseDto createUserDetails(CreateUserRequestDto createUserRequest, String operation);

    List<UserListResponseDto> getUserList(Integer pageNumber, Integer pageSize, String operation);

    List<GroupListResponseDto> getGroupList(Integer pageNumber, Integer pageSize,
                                            String username, String fetchGroupDetailsList);
}
