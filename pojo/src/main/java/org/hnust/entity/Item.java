package org.hnust.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private String img;
    private Integer status;     // 0代表未审核，1代表审核失败，2代表未解决，3代表已解决
    private Integer tag;        // 物品类型： 0日常用品 1重要物品
    private Long userId;
    private Integer isLost;     // 0代表丢失，1代表招领
    private LocalDateTime publishTime;
    private String region;
    private Integer color;
}
