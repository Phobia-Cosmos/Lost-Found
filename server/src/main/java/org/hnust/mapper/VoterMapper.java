package org.hnust.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoterMapper {

    @Insert("INSERT INTO vote (suggestion_id, user_id) values (#{id}, #{userId})")
    void insert(Long id, Long userId);

    @Delete("Delete from vote where id = #{id}")
    void deleteById(Long id);
}
