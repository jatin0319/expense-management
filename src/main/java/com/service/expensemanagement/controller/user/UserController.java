package com.service.expensemanagement.controller.user;

import com.service.expensemanagement.dto.*;
import com.service.expensemanagement.helper.JwtUtil;
import com.service.expensemanagement.service.user.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final String CREATE_GROUP_DETAILS = "create-group-details";
    private static final String CREATE_USER_DETAILS = "create-user-details";
    private static final String FETCH_USER_DETAILS_LIST = "";
    private static final String FETCH_GROUP_DETAILS_LIST = "";
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation(value = "Api to create user")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<CreateUserResponseDto> createUserDetails(@Valid @RequestBody CreateUserRequestDto createUserRequest) {
        return new ResponseEntity<>(userService.createUserDetails(createUserRequest, CREATE_USER_DETAILS), HttpStatus.OK);
    }

    @ApiOperation(value = "Api to create user group")
    @RequestMapping(value = "/group/add", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<CreateGroupResponseDto> createGroupDetails(@Valid @RequestBody CreateGroupRequestDto createGroupRequest) {
        return new ResponseEntity<>(userService.createGroupDetails(createGroupRequest, CREATE_GROUP_DETAILS), HttpStatus.OK);
    }

    @ApiOperation(value = "Api to get list of users")
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<UserListResponseDto>> getUserList(@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                 @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber) {
        List<UserListResponseDto> userListResponse =
                userService.getUserList(pageNumber, pageSize, FETCH_USER_DETAILS_LIST);
        return new ResponseEntity<>(userListResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Api to get list of groups")
    @RequestMapping(value = "/group/list", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<GroupListResponseDto>> getGroupList(@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                   @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                   HttpServletRequest request) {
        String username = jwtUtil.extractUsername(request.getHeader("Authorization").split("Bearer ")[1]);
        List<GroupListResponseDto> contactList =
                userService.getGroupList(pageNumber, pageSize, username, FETCH_GROUP_DETAILS_LIST);
        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }
}
