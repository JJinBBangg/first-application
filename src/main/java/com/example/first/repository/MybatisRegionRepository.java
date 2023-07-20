package com.example.first.repository;


import com.example.first.entity.Region;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MybatisRegionRepository {
    @Insert("""
            <script>
            INSERT INTO REGIONLIST(regionParent, regionChild, nx, ny)
            VALUES  
            <foreach collection="list" item="region" separator=",">
                (#{region.regionParent}, #{region.regionChild}, #{region.nx}, #{region.ny})
            </foreach>
            </script>
            """)
    int saveAll(List<Region> list);

    @Select("""
            SELECT * FROM REGIONLIST
            WHERE regionChild = #{region}
            """)
    Optional<Region> findByRegion(String region);

    @Delete("""
            DELETE FROM REGIONLIST;
            """)
    void deleteAll();
}


