package com.eason.blog.Service;

import com.eason.blog.domain.Blog;
import com.eason.blog.mapper.BlogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class BlogService {

    @Resource
    private BlogMapper blogMapper;

    public List<Blog> list(){
        return blogMapper.selectByExample(null);
    }

}
