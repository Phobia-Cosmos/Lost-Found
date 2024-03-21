package org.hnust.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String img;
    private Integer tag;        // 物品类型： 0日常用品 1重要物品

    private Long userId;
    private Integer isLost;     // 0代表丢失，1代表招领

    @ApiModelProperty(example = "2022-02-24T10:15:30Z")
    private LocalDateTime startTime;
    @ApiModelProperty(example = "2022-02-25T10:15:30Z")
    private Timestamp endTime;
}
