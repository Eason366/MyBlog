package com.eason.blog.service;

import com.eason.blog.domain.Blog;
import com.eason.blog.domain.BlogExample;
import com.eason.blog.mapper.BlogMapper;
import com.eason.blog.req.BlogQueryReq;
import com.eason.blog.req.BlogSaveReq;
import com.eason.blog.req.CategoryQueryReq;
import com.eason.blog.resp.BlogQueryResp;
import com.eason.blog.resp.PageResp;
import com.eason.blog.util.CopyUtil;
import com.eason.blog.util.SnowFlake;
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

    @Resource
    private SnowFlake snowFlake;

    public PageResp<BlogQueryResp> list(BlogQueryReq req){
        BlogExample blogExample = new BlogExample();
        BlogExample.Criteria criteria = blogExample.createCriteria();

        if (!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        if (!ObjectUtils.isEmpty(req.getCategory())){
            criteria.andCategoryEqualTo(req.getCategory());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Blog> blogsList = blogMapper.selectByExample(blogExample);


        PageInfo<Blog> pageInfo = new PageInfo<>(blogsList);
        LOG.info("Total Rows：{}", pageInfo.getTotal());
        LOG.info("Total Pages：{}", pageInfo.getPages());

        List<BlogQueryResp> list = CopyUtil.copyList(blogsList, BlogQueryResp.class);

        PageResp<BlogQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;

    }


    public void save(BlogSaveReq req){
        Blog blog = CopyUtil.copy(req,Blog.class);
        if (ObjectUtils.isEmpty(req.getId())){
            // insert
            blog.setId(snowFlake.nextId());
            blog.setVoteCount(0);
            blog.setViewCount(0);
            if (ObjectUtils.isEmpty(req.getCover())) blog.setCover("https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png");
            blogMapper.insert(blog);
        }else {
            //update
            blogMapper.updateByPrimaryKey(blog);
        }

    }

    public void delete(Long id){
        blogMapper.deleteByPrimaryKey(id);
    }




}
