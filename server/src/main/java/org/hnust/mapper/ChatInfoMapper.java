package org.hnust.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hnust.entity.ChatInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatInfoMapper {

    int insert(ChatInfo chatInfo);

    @Select("select count(*) from chat_info where user_id = #{chatUserId} and chat_user_id = #{userId} and isRead = 0")
    Integer selectUnReadChatNum(@Param("userId") Long userId, @Param("chatUserId") Long chatUserId);

    List<ChatInfo> selectUserChat(@Param("userId") Long userId, @Param("chatUserId") Long chatUserId);

    @Update("update chat_info set isRead = 1 where user_id = #{userId} and chat_user_id = #{chatUserId}")
    void updateRead(@Param("userId") Long userId, @Param("chatUserId") Long chatUserId);

}