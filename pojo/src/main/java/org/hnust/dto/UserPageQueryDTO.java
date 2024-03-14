package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageQueryDTO implements Serializable {

    private String name;
    private String phone;
    private String email;
    private String school;
    private int page;
    private int pageSize;
}
