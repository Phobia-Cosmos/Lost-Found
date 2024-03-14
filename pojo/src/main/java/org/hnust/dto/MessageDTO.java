package org.hnust.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageDTO implements Serializable {
    private Long id;
    private Long itemId;
    private Long lostUserId;
    private Long foundUserId;
    private String content;
}
