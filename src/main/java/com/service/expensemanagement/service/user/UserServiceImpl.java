package com.service.expensemanagement.service.user;

import com.service.expensemanagement.dao.GroupRepository;
import com.service.expensemanagement.dao.UserRepository;
import com.service.expensemanagement.dao.UserToGroupMappingRepository;
import com.service.expensemanagement.dto.*;
import com.service.expensemanagement.models.Group;
import com.service.expensemanagement.models.User;
import com.service.expensemanagement.models.UserToGroupMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserToGroupMappingRepository userToGroupMappingRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, GroupRepository groupRepository,
                           UserToGroupMappingRepository userToGroupMappingRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userToGroupMappingRepository = userToGroupMappingRepository;
    }

    @Override
    public CreateGroupResponseDto createGroupDetails(CreateGroupRequestDto createGroupRequest,
                                                     String operation) {
        List<UserToGroupMapping> userToGroupMappingList = new ArrayList<>();
        this.validateGroupRequest(createGroupRequest);

        Group group = Group.builder()
                .groupName(createGroupRequest.getName())
                .description(createGroupRequest.getDescription())
                .build();

        createGroupRequest.getUserIds()
                .forEach(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found for id" + userId));
                    userToGroupMappingList.add(UserToGroupMapping.builder().group(group).user(user).build());
                });


        groupRepository.save(group);
        userToGroupMappingRepository.saveAll(userToGroupMappingList);
        return CreateGroupResponseDto.builder()
                .message("Successfully created group")
                .build();
    }

    private void validateGroupRequest(CreateGroupRequestDto createGroupRequest) {
        if (createGroupRequest.getUserIds() == null)
            throw new RuntimeException("User Ids cannot be null");

        if (createGroupRequest.getUserIds().size() < 2)
            throw new RuntimeException("At least Add 2 user to create group");
    }


    @Override
    public CreateUserResponseDto createUserDetails(CreateUserRequestDto createUserRequest,
                                                   String operation) {
        Optional<User> optionalUser = userRepository.findByUsername(createUserRequest.getUsername());
        if (optionalUser.isPresent())
            throw new RuntimeException("This username is already taken please try another one");

        User user = User.builder()
                .username(createUserRequest.getUsername())
                .password(createUserRequest.getPassword())
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .build();

        userRepository.save(user);
        return CreateUserResponseDto.builder()
                .message("Successfully created User")
                .build();
    }

    @Override
    public List<UserListResponseDto> getUserList(Integer pageNumber, Integer pageSize, String search, String sort,
                                                 String sortDir, String operation) {
        this.validateQueryParameter(pageNumber, pageSize, search, sortDir);
        List<UserListResponseDto> userListResponse = new ArrayList<>();
        Sort sortBy = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sort).ascending() : Sort.by(sort).descending() ;

        Pageable pagination = PageRequest.of(pageNumber, pageSize, sortBy);
        List<User> userList =userRepository.findByFirstNameContainingOrLastNameContaining(search, search,pagination);
        userList.forEach(user -> {
            userListResponse.add(UserListResponseDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .userId(user.getId())
                    .build());
        });
        return userListResponse;
    }

    @Override
    public List<GroupListResponseDto> getGroupList(Integer pageNumber, Integer pageSize, String username, String fetchGroupDetailsList) {
        List<GroupListResponseDto> groupListResponse = new ArrayList<>();
        Pageable pagination = PageRequest.of(pageNumber, pageSize);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));

        List<UserToGroupMapping> userToGroupMappingList = userToGroupMappingRepository.findByUser(user,pagination);
        userToGroupMappingList.forEach(item -> {
            groupListResponse.add(GroupListResponseDto.builder()
                    .groupId(item.getGroup().getId())
                    .name(item.getGroup().getGroupName())
                    .description(item.getGroup().getDescription())
                    .build());
        });
        return groupListResponse;
    }

    private void validateQueryParameter(Integer pageNumber, Integer pageSize, String search, String sortDir){
        if (pageNumber <0 || pageNumber >999)
            throw new RuntimeException("Invalid page number");

        if (pageSize <0 || pageSize >999)
            throw new RuntimeException("Invalid page size");

        if (!sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) && !sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) )
            throw new RuntimeException("Invalid sort direction");
    }
}
