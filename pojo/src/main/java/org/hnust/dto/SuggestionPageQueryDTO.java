package org.hnust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionPageQueryDTO implements Serializable {
    private Long userId;
    private Integer tag; // 需要人为设置值来指代意义
    private Integer pollCount;
    private Integer status;
    private Long validatorId;
    private int page;
    private int pageSize;
}