package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class ItemDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String img;
    private Integer tag;        //物品类型： 0日常用品 1重要物品
    private Long userId;
    private Integer isLost;     //0代表丢失，1代表招领
    private Timestamp startTime;
    private Timestamp endTime;
}
