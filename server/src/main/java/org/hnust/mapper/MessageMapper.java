package org.hnust.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.hnust.dto.MessageQueryDTO;
import org.hnust.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {

    List<Message> loadMessageList(MessageQueryDTO messageQueryDTO, Short loadtype);

    @Insert("INSERT INTO messages (item_id, lost_user_id, found_user_id, content, create_time, update_time) " +
            "VALUES (#{itemId}, #{lostUserId}, #{foundUserId}, #{content}, #{createTime}, #{updateTime})")
    void insert(Message message);

    void deleteByIds(List<Long> ids);
}