package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private String username;
    private String password;
    private String phone;
    private String email;
}
