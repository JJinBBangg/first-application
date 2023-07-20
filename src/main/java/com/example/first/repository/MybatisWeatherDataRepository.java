package com.example.first.repository;


import com.example.first.entity.Region;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MybatisWeatherDataRepository {
    @Insert("""
            INSERT INTO WEATHERDATA VALUES()
            WHRER 
            """)
    void saveAll();
    @Delete("""
            DELETE FROM WEATHERDATA;
            """)
    void deleteAll();

}


