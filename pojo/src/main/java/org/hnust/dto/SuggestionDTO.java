package org.hnust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionDTO implements Serializable {
    // 这个字段不是在新增时使用，而是在修改时使用，因为修改和新增使用的是一个DTO
    private Long id;
    private Long userId;
    private Integer tag; // 需要人为设置值来指代意义
    private String content;
}
