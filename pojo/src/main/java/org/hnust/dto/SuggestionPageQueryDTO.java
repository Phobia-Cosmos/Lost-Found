package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SuggestionPageQueryDTO implements Serializable {
    private int page;
    private int pageSize;

    private Long userId;
    private Integer tag; // 需要人为设置值来指代意义
    private Integer pollCount;

    private Integer status;
    private Long validatorId;

}