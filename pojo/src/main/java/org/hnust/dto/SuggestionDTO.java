package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SuggestionDTO implements Serializable {
    private Long id;
    private Long userId;
    private Integer tag; // 需要人为设置值来指代意义
    private String content;
}
