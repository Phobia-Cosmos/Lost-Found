package org.hnust.service;

import cn.hutool.core.bean.BeanUtil;
import org.hnust.context.BaseContext;
import org.hnust.dto.UserDTO;
import org.hnust.entity.ChatGroup;
import org.hnust.entity.User;
import org.hnust.mapper.ChatGroupMapper;
import org.hnust.vo.ChatGroupVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class ChatGroupService {

    @Resource
    private ChatGroupMapper chatGroupMapper;
    @Resource
    ChatInfoService chatInfoService;
    @Resource
    UserService userService;

    public void add(ChatGroup chatGroup) {
        ChatGroup dbChatGroup = chatGroupMapper.selectByChatUserIdAndUserId(chatGroup.getChatUserId(), chatGroup.getUserId());
        if (dbChatGroup == null) {
            chatGroupMapper.insert(chatGroup);
        }
        dbChatGroup = chatGroupMapper.selectByChatUserIdAndUserId(chatGroup.getUserId(), chatGroup.getChatUserId());
        if (dbChatGroup == null) {
            dbChatGroup = new ChatGroup();
            dbChatGroup.setChatUserId(chatGroup.getUserId());
            dbChatGroup.setUserId(chatGroup.getChatUserId());
            chatGroupMapper.insert(dbChatGroup);
        }
    }

    public void deleteById(Integer id) {
        chatGroupMapper.deleteById(id);
    }

    public List<ChatGroupVO> selectUserGroup() {
        List<ChatGroupVO> chatGroupList = new ArrayList<>();
        UserDTO currentUser = BaseContext.getCurrentUser();
        Long userId = currentUser.getId();

        List<ChatGroup> userChatGroupList = chatGroupMapper.selectByUserId(userId);
        for (ChatGroup chatGroup : userChatGroupList) {
            // 每一个聊天对象的ID
            Long chatUserId = chatGroup.getChatUserId();
            // 每一个对象各有多少未读
            Integer unReadChatNum = chatInfoService.selectUnReadChatNum(userId, chatUserId);

            ChatGroupVO tmp = new ChatGroupVO();
            BeanUtil.copyProperties(chatGroup, tmp);
            tmp.setChatNum(unReadChatNum);

            User chatUser = userService.getById(chatUserId);
            tmp.setChatUserName(chatUser.getName());
            tmp.setChatUserAvatar(chatUser.getAvatar());
            chatGroupList.add(tmp);
        }
        return chatGroupList;
    }

}