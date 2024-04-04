package org.hnust.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.hnust.dto.ItemPageDTO;
import org.hnust.entity.Item;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Insert("INSERT INTO items(name, description, img, status, tag, user_id, is_lost, publish_time, region, color)" +
            " VALUES" +
            " (#{name}, #{description}, #{img}, #{status}, #{tag}, #{userId}, #{isLost}, #{publishTime}, #{region}," +
            "#{color})")
    void insert(Item item);


    void update(Item item);

    void deleteByIds(List<Long> ids);

    Page<Item> pageQuery(@Param("query") ItemPageDTO itemPageDTO, @Param("role") Integer role);

    @Select("select * from items where id = #{id}")
    Item getById(Long id);

    @Select("SELECT * FROM items")
    List<Item> selectAll();
}
