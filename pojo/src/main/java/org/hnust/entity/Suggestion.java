package org.hnust.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {
    private Long id;
    private Long userId;
    private Integer tag; // 需要人为设置值来指代意义
    private Integer pollCount;
    private String content;
    private Integer status;     // 0:未审核，1:审核失败，2:待解决，3：已解决
    private Timestamp publishTime;
    private String validateMsg;               //     审核意见
    private Long validatorId;
    // private List<Long> votedUser;       // 放到另一张表中存储建议和用户的关系
}