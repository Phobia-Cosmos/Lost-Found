
package org.hnust.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private Long id;
    private String username;
    private String salt;
    private String password;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private Integer role;       // 0代表管理员，1代表普通用户
    private Integer reputation;     //只能由admin修改
    private String school;
    private Timestamp createTime;
}
