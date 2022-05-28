package com.eason.blog.service;

import com.eason.blog.domain.Blog;
import com.eason.blog.domain.BlogExample;
import com.eason.blog.mapper.BlogMapper;
import com.eason.blog.req.BlogReq;
import com.eason.blog.resp.BlogResp;
import com.eason.blog.resp.PageResp;
import com.eason.blog.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class BlogService {
    private static final Logger LOG = LoggerFactory.getLogger(BlogService.class);

    @Resource
    private BlogMapper blogMapper;

    public PageResp<BlogResp> list(BlogReq req){
        BlogExample blogExample = new BlogExample();
        BlogExample.Criteria criteria = blogExample.createCriteria();

        if (!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Blog> blogsList = blogMapper.selectByExample(blogExample);


        PageInfo<Blog> pageInfo = new PageInfo<>(blogsList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<BlogResp> list = CopyUtil.copyList(blogsList, BlogResp.class);

        PageResp<BlogResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;

    }

}
