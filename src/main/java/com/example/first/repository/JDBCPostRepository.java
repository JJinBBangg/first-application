package com.example.first.repository;


import com.example.first.entity.Post;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JDBCPostRepository implements PostRepository{

    private final DataSource dataSource;

    @Override
    public int save(Post post) {
        String sql = """
                INSERT INTO Post(title, content)
                VALUES(?, ? )
                """;
        int result = 0;
        try (Connection con = dataSource.getConnection();
               PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, post.getTitle());
            st.setString(2, post.getContent());
            result = st.executeUpdate();
        } catch(Exception e) {
              e.printStackTrace();
        }
        return result;

    }

    @Override
    public int count() {
        String sql = """
                SELECT COUNT(ID) count FROM POST
                """;
        int result = 0;
        try (Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            result = rs.getInt("count");
        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int deleteAll() {
        String sql ="DELETE FROM POST";
        int result = 0;

        try (Connection con = dataSource.getConnection();
                Statement st = con.createStatement()) {
            result = st.executeUpdate(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Post> findAll() {
        List<Post> postList = new ArrayList<>();
        String sql ="SELECT * FROM POST";
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while(rs.next()){
                Post post = new Post();
//                post.setId(rs.getInt("id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                postList.add(post);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return postList;
    }

    @Override
    public Optional<Post> findById(Long id){
        String sql = """
                SELECT * FROM POST WHERE id = ?
                """;
        List<Post> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                list.add(Post.builder()
                        .id(rs.getLong("id"))
                        .title(rs.getString("title"))
                        .content(rs.getString("content"))
                        .build());
                }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return Optional.ofNullable(list.get(0));
    }

    @Override
    public int saveAll(List<Post> posts) {

        return 0;
    }

    @Override
    public List<Post> findPage(int page) {
        return null;
    }

    @Override
    public int edit(Post post) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
