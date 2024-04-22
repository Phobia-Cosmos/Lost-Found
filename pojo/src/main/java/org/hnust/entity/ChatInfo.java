package org.hnust.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatInfo {

    private Long id;
    private Long chatUserId;
    private Long userId;
    private String text;
    private Integer isRead;
    private LocalDateTime time;

    private String chatUserName;
    private String chatUserAvatar;
    private String userName;
    private String userAvatar;
}