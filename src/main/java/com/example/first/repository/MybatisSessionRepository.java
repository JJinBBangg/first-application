package com.example.first.repository;

import com.example.first.entity.UserSession;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MybatisSessionRepository {
    @Insert("""
            INSERT INTO usersession(accessToken, userId)
            VALUES( #{accessToken} , #{userId} )
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void save(UserSession session);

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
    Optional<UserSession> findByAccessToken(String accessToken);
}
