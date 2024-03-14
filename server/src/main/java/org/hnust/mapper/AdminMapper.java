package org.hnust.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.hnust.dto.UserPageQueryDTO;
import org.hnust.entity.User;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Insert("INSERT INTO users (username, salt, password, name, phone, email, avatar, role, reputation, school, " +
            "create_time) " +
            "VALUES (#{username}, #{salt}, #{password}, #{name}, #{phone}, #{email}, #{avatar}, #{role}, #{reputation}, #{school}, #{createTime})")
    void register(User user);

    void deleteByIds(List<Long> ids);

    void update(User user);

    @Select("select * from users where username = #{username}")
    User selectByUsername(String username);

    @Select("select * from users where phone = #{phone}")
    User selectByPhone(String phone);

    @Select("select * from users where email = #{email}")
    User selectByEmail(String email);

    @Select("<script>" +
            "SELECT * FROM users WHERE" +
            "<if test='username != null'> username = #{username} OR </if>" +
            "<if test='phone != null'> phone = #{phone} OR </if>" +
            "<if test='email != null'> email = #{email} </if>" +
            "</script>")
    User selectByCriteria(@Param("username") String username,
                          @Param("phone") String phone,
                          @Param("email") String email);


    @Select("select * from users where id = #{id}")
    User getById(Long empId);

    Page<User> pageQuery(UserPageQueryDTO userPageQueryDTO);
}