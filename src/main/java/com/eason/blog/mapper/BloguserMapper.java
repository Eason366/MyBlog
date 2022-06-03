package com.eason.blog.mapper;

import com.eason.blog.domain.Bloguser;
import com.eason.blog.domain.BloguserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BloguserMapper {
    long countByExample(BloguserExample example);

    int deleteByExample(BloguserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Bloguser record);

    int insertSelective(Bloguser record);

    List<Bloguser> selectByExample(BloguserExample example);

    Bloguser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Bloguser record, @Param("example") BloguserExample example);

    int updateByExample(@Param("record") Bloguser record, @Param("example") BloguserExample example);

    int updateByPrimaryKeySelective(Bloguser record);

    int updateByPrimaryKey(Bloguser record);
}