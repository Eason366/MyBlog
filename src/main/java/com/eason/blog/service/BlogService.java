package com.eason.blog.service;

import com.eason.blog.domain.Blog;
import com.eason.blog.domain.BlogExample;
import com.eason.blog.domain.Content;
import com.eason.blog.exception.BusinessException;
import com.eason.blog.exception.BusinessExceptionCode;
import com.eason.blog.mapper.BlogMapper;
import com.eason.blog.mapper.ContentMapper;
import com.eason.blog.mapper.MyDocMapper;
import com.eason.blog.req.BlogQueryReq;
import com.eason.blog.req.BlogSaveReq;
import com.eason.blog.resp.BlogQueryResp;
import com.eason.blog.resp.PageResp;
import com.eason.blog.util.CopyUtil;
import com.eason.blog.util.RedisUtil;
import com.eason.blog.util.RequestContext;
import com.eason.blog.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class BlogService {
    private static final Logger LOG = LoggerFactory.getLogger(BlogService.class);

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private MyDocMapper myDocMapper;

    @Resource
    private BloguserService bloguserService;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    public RedisUtil redisUtil;

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
        if (!ObjectUtils.isEmpty(req.getCategories())){
            List<String> list = Arrays.asList(req.getCategories().split(","));
            criteria.andCategoryIn(list);
        }
        if (!ObjectUtils.isEmpty(req.getUser())){
            criteria.andUserEqualTo(req.getUser());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Blog> blogsList = blogMapper.selectByExample(blogExample);

        for (Blog b : blogsList) {
            b.setUser(bloguserService.getName(b.getUser()));
        }

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
        Content content = CopyUtil.copy(req,Content.class);
        if (ObjectUtils.isEmpty(req.getId())){
            // insert
            blog.setId(snowFlake.nextId());
            blog.setVoteCount(0);
            blog.setViewCount(0);
            if (ObjectUtils.isEmpty(req.getCover())) blog.setCover("https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png");
            blogMapper.insert(blog);

            content.setId(blog.getId());
            contentMapper.insert(content);
        }else {
            //update
            blogMapper.updateByPrimaryKey(blog);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);

            if (count==0){
                contentMapper.insert(content);
            }
        }

    }

    public void delete(Long id){
        blogMapper.deleteByPrimaryKey(id);
    }


    public BlogQueryResp list(Long id){
        Blog blog = blogMapper.selectByPrimaryKey(id);
        blog.setUser(bloguserService.getName(blog.getUser()));
        BlogQueryResp blogQueryResp = CopyUtil.copy(blog, BlogQueryResp.class);

        return blogQueryResp;
    }

    public String findmdContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getMdcontent();
        }
    }

    public String findhtmlContent(Long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        myDocMapper.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getHtmlcontent();
        }
    }

    public void vote(Long id) {
//        myDocMapper.increaseVoteCount(id);
//        No repeat voting within 24 hours
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            myDocMapper.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
    }
}
