package org.hnust.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voters {
    private Long id;
    private Long userId;
    private Long suggestionId;
}
