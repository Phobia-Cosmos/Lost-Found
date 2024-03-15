package org.hnust.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private Long id;
    private Long itemId;
    private Long sender;
    private Long receiver;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
}