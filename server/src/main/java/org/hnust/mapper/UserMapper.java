package org.hnust.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hnust.dto.UserPageQueryDTO;
import org.hnust.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (username, salt, password, name, phone, email, avatar, role, reputation, school, " +
            "create_time) " +
            "VALUES (#{username}, #{salt}, #{password}, #{name}, #{phone}, #{email}, #{avatar}, #{role}, #{reputation}, #{school}, #{createTime})")
    void register(User user);

    void deleteByIds(List<Long> ids);

    void update(User user);

    @Select("SELECT * FROM users WHERE username IS NOT NULL AND username != '' AND BINARY username = #{username}")
    User selectByUsername(String username);

    @Select("SELECT * FROM users WHERE phone IS NOT NULL AND phone != '' AND BINARY phone = #{phone}")
    User selectByPhone(String phone);

    @Select("SELECT * FROM users WHERE email IS NOT NULL AND email != '' AND BINARY email = #{email}")
    User selectByEmail(String email);


    @Select("select * from users where id IS NOT NULL AND id = #{id}")
    User getById(Long id);

    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);

}