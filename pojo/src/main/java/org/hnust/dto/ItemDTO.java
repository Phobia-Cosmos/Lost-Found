package org.hnust.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class ItemDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String img;
    private Integer tag;        // 物品类型： 0日常用品 1重要物品

    private Long userId;
    private Integer isLost;     // 0代表丢失，1代表招领

    @ApiModelProperty(example = "2022-02-24T10:15:30Z")
    private Timestamp startTime;
    @ApiModelProperty(example = "2022-02-25T10:15:30Z")
    private Timestamp endTime;
}
