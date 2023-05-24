package com.service.expensemanagement.controller.token;

import com.service.expensemanagement.dto.LoginRequestDto;
import com.service.expensemanagement.dto.LoginResponseDto;
import com.service.expensemanagement.helper.JwtUtil;
import com.service.expensemanagement.service.token.CustomUserDetailsServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class TokenController {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    public TokenController(JwtUtil jwtUtil, CustomUserDetailsServiceImpl customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    @ApiOperation(value = "Api to Generate jwt token")
    @RequestMapping(value = "/token", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<LoginResponseDto> generateToken(@Valid @RequestBody LoginRequestDto loginDto) {
        UserDetails userDetails= customUserDetailsService.loadUserByUsername(loginDto.getUsername());
        if (!userDetails.getPassword().equals(loginDto.getPassword()))
            throw new RuntimeException("Invalid user or password");
        LoginResponseDto loginResponseDto = LoginResponseDto.builder().token(jwtUtil.generateToken(userDetails)).build();
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
    }
}
