package com.eason.blog.service;

import com.eason.blog.domain.Blog;
import com.eason.blog.domain.BlogExample;
import com.eason.blog.mapper.BlogMapper;
import com.eason.blog.req.BlogReq;
import com.eason.blog.resp.BlogResp;
import com.eason.blog.util.CopyUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class BlogService {

    @Resource
    private BlogMapper blogMapper;

    public List<BlogResp> list(BlogReq req){
        BlogExample blogExample = new BlogExample();
        BlogExample.Criteria criteria = blogExample.createCriteria();

        if (!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        List<Blog> blogsList = blogMapper.selectByExample(blogExample);

        List<BlogResp> list = CopyUtil.copyList(blogsList, BlogResp.class);

        return list;

    }

}
