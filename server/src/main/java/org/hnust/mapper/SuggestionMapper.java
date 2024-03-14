package org.hnust.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hnust.dto.SuggestionPageQueryDTO;
import org.hnust.entity.Suggestion;

import java.util.List;

@Mapper
public interface SuggestionMapper {

    @Insert("INSERT INTO suggestions (user_id, tag, poll_count, content, status, publish_time, validate_msg, " +
            "validator_id) " +
            "VALUES (#{userId}, #{tag}, #{pollCount}, #{content}, #{status}, #{publishTime}, #{validateMsg}, #{validatorId})")
    void insert(Suggestion suggestion);

    void update(Suggestion suggestion);

    void deleteByIds(List<Long> ids);

    @Select("SELECT * from suggestions where id = #{id}")
    Suggestion getById(Long id);

    Page<Suggestion> pageQuery(SuggestionPageQueryDTO suggestionPageQueryDTO, Integer role);
}