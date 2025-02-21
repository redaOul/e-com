package com.redaeilco.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String userName;
    private String role;
}
