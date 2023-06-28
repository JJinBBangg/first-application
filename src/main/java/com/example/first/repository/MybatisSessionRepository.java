package com.example.first.repository;

import com.example.first.entity.AuthUser;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface MybatisSessionRepository {
    @Insert("""
            INSERT INTO USERSESSION(accessToken, userId)
            VALUES( #{accessToken} , #{userId} )
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void save(AuthUser session);

    @Select("""
            SELECT COUNT(id) FROM USERSESSION
            """)
    int count();

    @Delete("""
            DELETE FROM USERSESSION
            """)
    int deleteAll();

    @Select("""
            SELECT * FROM USERSESSION
            WHERE accessToken LIKE #{accessToken}
            """)
    Optional<AuthUser> findByAccessToken(String accessToken);
}
