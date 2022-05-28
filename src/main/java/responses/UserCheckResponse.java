package com.example.demo.responses;

import com.example.demo.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckResponse {

    private boolean success;
    private String message;

    private UserEntity user;
}