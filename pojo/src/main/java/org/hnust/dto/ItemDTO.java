package org.hnust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
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
    private Integer color;
    private String region;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;
}