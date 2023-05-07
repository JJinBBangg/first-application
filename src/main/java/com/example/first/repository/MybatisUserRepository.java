package com.example.first.repository;

import com.example.first.entity.User;
import com.example.first.request.Login;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MybatisUserRepository {

    @Select("""
            SELECT * FROM MEMBER 
            """)
    List<User> findAll();

    @Select("""
            SELECT * FROM MEMBER
            WHERE email = #{email} and password = #{password}
            """)
    Optional<User> findByEmailAndPassword(Login login);

    Optional<User> getUserWithSession(Long userId);

    @Select("""
            SELECT * FROM MEMBER
            WHERE id = #{id}
            """)
    Optional<User> findById(Long id);

    @Insert("""
            INSERT INTO MEMBER(name, email, password)
            VALUES(#{name}, #{email}, #{password})
            """)
    @Options(useGeneratedKeys = true, keyColumn="id", keyProperty="id")
    int save(User user);

    @Delete("""
            DELETE FROM MEMBER
            """)
    int deleteAll();

    @Select("""
            SELECT * FROM MEMBER
            WHERE email = #{email}
            """)
    Optional<User> findByEmail(User user);
}
