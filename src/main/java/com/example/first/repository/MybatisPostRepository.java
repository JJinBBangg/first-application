package com.example.first.repository;


import com.example.first.entity.Files;
import com.example.first.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MybatisPostRepository {


    @Insert("""
            INSERT INTO POST(title, content, userId)
            VALUES(#{title}, #{content}, #{userId} )
            """)
    @Options(useGeneratedKeys = true, keyColumn="id", keyProperty="id")
    int save(Post post);

    @Select("""
            SELECT COUNT(ID) count FROM POST
            """)
    int count();

    @Delete("""
            DELETE FROM POST
            """)
    int deleteAll();


    List<Post> getPostWithFilesAndUserList(@Param("page") int page,@Param("search") String search,@Param("type") String type);

    List<Post> getPostWithFilesAndUserListCount(@Param("search") String search,@Param("type") String type);
    Optional<Post> getPostWithFilesAndUser(Long postId);
    @Select("""
            SELECT * FROM POST
            """)
    List<Post> findAll();

    @Select("""
            SELECT * FROM POST WHERE id = #{id}
            """)
    Optional<Post> findById(Long id);

    @Insert("""
            <script>
            INSERT INTO POST(title, content, userId)
            VALUES  
            <foreach collection="list" item="post" separator=",">
                (#{post.title}, #{post.content}, #{post.userId})
            </foreach>
            </script>
            """)
    int saveAll(List<Post> list);

    @Update("""
            UPDATE POST SET
                title = #{title},
                content = #{content}
            WHERE ID = #{id}
            """)
    int edit(Post post);

    @Delete("""
            DELETE FROM POST WHERE id = #{id}
            """)
    int delete(Long id);

    @Insert("""
            INSERT INTO FILES(postId, fileName) 
            VALUES( #{postId}, #{fileName})
            """)
    void saveFile(Files file);

    @Update("""
            UPDATE POST
            SET hit = hit + 1
            WHERE id = #{id}
            """)
    int hitPlus(Long id);



}


