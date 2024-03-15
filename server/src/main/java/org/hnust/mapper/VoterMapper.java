package org.hnust.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface VoterMapper {

    @Insert("INSERT INTO voters (suggestion_id, user_id, operation) values (#{id}, #{userId}, #{operation})")
    void insert(Long id, Long userId, Long operation);

    @Delete("Delete from voters where id = #{id}")
    void deleteById(Long id);

    // @Select("SELECT COUNT(*) > 0 FROM voters WHERE user_id = #{userId}")
    // @Select("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE NULL END FROM voters WHERE user_id = #{userId}")
    // @Select("SELECT CASE " +
    //     "    WHEN EXISTS (SELECT 1 FROM voters WHERE user_id = #{userId} AND operation = 1) THEN TRUE " +
    //     "    WHEN EXISTS (SELECT 1 FROM voters WHERE user_id = #{userId}) THEN FALSE " +
    //     "    ELSE NULL " +
    //     "END")
    @Select("SELECT operation from voters where user_id = #{userId} and suggestion_id = #{suggestionId}")
    Long getOpByUserId(Long suggestionId, Long userId);

    @Update("Update voters set operation = #{operation} where user_id = #{userId} and suggestion_id = #{suggestionId}")
    void modify(Long suggestionId, Long userId, Long operation);
}
