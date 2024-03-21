package org.hnust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageQueryDTO implements Serializable {

    private String username;
    private String phone;
    private String email;
    private String school;
    private Integer role;       // 0代表管理员，1代表普通用户
    private int page;
    private int pageSize;
}
