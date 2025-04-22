package com.bithaus.apirestspring.adapter.in.rest.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String username;
    private String password;
}

