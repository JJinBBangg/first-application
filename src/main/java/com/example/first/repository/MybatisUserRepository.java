package com.example.first.repository;

import com.example.first.entity.User;
import com.example.first.request.Login;
import com.example.first.request.UserEdit;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MybatisUserRepository {

    @Select("""
            SELECT * FROM MEMBER 
            ORDER BY 1 DESC
            LIMIT 30 OFFSET ${(page > 0) ? (page-1) * 30 : 0}
            """)
    List<User> findAll(int page);

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
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int save(User user);

    @Delete("""
            DELETE FROM MEMBER
            """)
    int deleteAll();

    @Select("""
            SELECT * FROM MEMBER
            WHERE email = #{email}
            """)
    Optional<User> findByEmail(String email);

    @Insert("""
                <script>
                    INSERT INTO MEMBER(email, name, password)
                    VALUES
                    <foreach collection="list" item="user" separator=",">
                        (#{user.email}, #{user.name}, #{user.password})
                    </foreach>
                </script>
            """)
    int saveAll(List<User> list);

    @Select("""
            SELECT * FROM MEMBER
            WHERE name = #{name}
            """)
    Optional<User> findByMame(String name);

    @Update("""
            <script>
            
            UPDATE MEMBER SET
                name = #{name},
                content = #{password}
            WHERE ID = #{userId}
            
            </script>
            """)
    void update(UserEdit userEdit);
}
