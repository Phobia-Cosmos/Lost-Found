package org.hnust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPageDTO implements Serializable {
    private int page;
    private int pageSize;

    private String name;
    private Integer status;     // 0代表未审核，1代表审核失败，2代表未解决，3代表已解决
    private Integer tag;        // 物品类型： 0日常用品 1重要物品
    private Long userId;
    private Integer isLost;     // 0代表丢失，1代表招领
    private Timestamp startTime;
    private Timestamp endTime;
}
