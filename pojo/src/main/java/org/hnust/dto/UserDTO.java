package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private String school;
}
