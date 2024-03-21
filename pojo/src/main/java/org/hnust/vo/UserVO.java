package org.hnust.vo;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserVO {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String avatar;
    private Integer role;       // 0代表管理员，1代表普通用户
    private Integer reputation;     //只能由admin修改
    private String school;
    private Timestamp createTime;
}
