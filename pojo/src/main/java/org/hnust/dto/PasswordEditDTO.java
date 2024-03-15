package org.hnust.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordEditDTO implements Serializable {

    //员工id
    private Long userId;
    private String oldPassword;
    private String newPassword;

}
