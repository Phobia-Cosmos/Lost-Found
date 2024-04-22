package org.hnust.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGroupVO {
    private Long id;
    private Long chatUserId;
    private Long userId;
    private String chatUserName;
    private String chatUserAvatar;
    private Integer chatNum;
}
