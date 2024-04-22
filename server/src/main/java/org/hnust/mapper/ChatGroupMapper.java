package org.hnust.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hnust.entity.ChatGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatGroupMapper {

    int insert(ChatGroup chatGroup);

    int deleteById(Integer id);

    @Select("select id, chat_user_id, user_id from chat_group where user_id = #{userId}")
    List<ChatGroup> selectByUserId(Long userId);

    @Select("select id, chat_user_id, user_id from chat_group where chat_user_id = #{chatUserId} and user_id = #{userId}")
    ChatGroup selectByChatUserIdAndUserId(@Param("chatUserId") Long chatUserId, @Param("userId") Long userId);
}