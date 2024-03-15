package org.hnust.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.hnust.dto.MessageQueryDTO;
import org.hnust.entity.Message;

import java.util.List;

@Mapper
public interface MessageMapper {

    List<Message> loadMessageList(@Param("messageQueryDTO") MessageQueryDTO messageQueryDTO, @Param("loadtype") Short loadtype);

    @Insert("INSERT INTO messages (item_id, sender, receiver, content, create_time, update_time) " +
            "VALUES (#{itemId}, #{sender}, #{receiver}, #{content}, #{createTime}, #{updateTime})")
    void insert(Message message);

    void deleteByIds(List<Long> ids);

    @Select("SELECT * from messages where id = #{id}")
    Message getById(Long id);
}