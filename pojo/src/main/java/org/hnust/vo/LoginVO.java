package org.hnust.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LoginVO implements Serializable {
    private Long id;
    private String username;
    private String avatar;
    private Integer role;       // 0代表管理员，1代表普通用户
    private String token;
}
