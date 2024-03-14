package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class MessageQueryDTO implements Serializable {
    private Long itemId;
    private Timestamp updateTime;
    private Integer size;
}
