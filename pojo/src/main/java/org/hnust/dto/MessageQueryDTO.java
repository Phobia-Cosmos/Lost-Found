package org.hnust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageQueryDTO implements Serializable {
    private Long itemId;
    private Timestamp updateTime;
    private Integer size;
}
