package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordEditDTO implements Serializable {

    //员工id
    private Long userId;

    //旧密码
    private String oldPassword;

    //新密码
    private String newPassword;

}