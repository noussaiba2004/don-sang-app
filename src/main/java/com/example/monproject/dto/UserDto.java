package com.example.monproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String role;
    private long centreID;
}

