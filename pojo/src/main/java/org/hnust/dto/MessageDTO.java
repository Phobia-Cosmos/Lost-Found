package org.hnust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {
    private Long id;
    private Long itemId;
    private Long sender;
    private Long receiver;
    private String content;
}
