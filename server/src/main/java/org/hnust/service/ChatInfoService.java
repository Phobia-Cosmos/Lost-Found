package org.hnust.service;

import lombok.extern.slf4j.Slf4j;
import org.hnust.context.BaseContext;
import org.hnust.dto.UserDTO;
import org.hnust.entity.ChatInfo;
import org.hnust.mapper.ChatInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ChatInfoService {

    @Resource
    private ChatInfoMapper chatInfoMapper;

    public void add(ChatInfo chatInfo) {
        chatInfo.setTime(LocalDateTime.now());
        chatInfoMapper.insert(chatInfo);
    }

    public Integer selectUnReadChatNum(Long userId, Long chatUserId) {
        return chatInfoMapper.selectUnReadChatNum(userId, chatUserId);
    }

    public List<ChatInfo> selectUserChat(Long chatUserId) {
        UserDTO currentUser = BaseContext.getCurrentUser();
        Long userId = currentUser.getId();
        log.info("当前用户ID为{}", userId);
        List<ChatInfo> chatInfos = chatInfoMapper.selectUserChat(userId, chatUserId);
        return chatInfos;
    }

    public void updateRead(Long chatUserId) {
        Long id = BaseContext.getCurrentUser().getId();
        chatInfoMapper.updateRead(id, chatUserId);
    }

}