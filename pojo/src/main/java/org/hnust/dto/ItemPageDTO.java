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
public class ItemPageDTO implements Serializable {
    private int page;
    private int pageSize;

    private String name;
    private Integer status;     // 0代表未审核，1代表审核失败，2代表未解决，3代表已解决
    private Integer tag;        // 物品类型： 0日常用品 1重要物品
    // 这个字段在用户中才会使用
    private Long userId;
    private Integer isLost;     // 0代表丢失，1代表招领
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @DateTimeFormat
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @DateTimeFormat
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
