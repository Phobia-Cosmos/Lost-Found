package org.hnust.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LoginVO implements Serializable {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private Integer reputation;     //只能由admin修改
    private String school;
    private Integer role;       // 0代表管理员，1代表普通用户
    private String token;
}
