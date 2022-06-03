package com.eason.blog.service;

import com.eason.blog.domain.Bloguser;
import com.eason.blog.domain.BloguserExample;
import com.eason.blog.mapper.BloguserMapper;
import com.eason.blog.req.BloguserQueryReq;
import com.eason.blog.req.BloguserSaveReq;
import com.eason.blog.resp.BloguserQueryResp;
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
public class BloguserService {
    private static final Logger LOG = LoggerFactory.getLogger(BloguserService.class);

    @Resource
    private BloguserMapper bloguserMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<BloguserQueryResp> list(BloguserQueryReq req){
        BloguserExample bloguserExample = new BloguserExample();
        bloguserExample.setOrderByClause("sort asc");
        BloguserExample.Criteria criteria = bloguserExample.createCriteria();

        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Bloguser> blogusersList = bloguserMapper.selectByExample(bloguserExample);


        PageInfo<Bloguser> pageInfo = new PageInfo<>(blogusersList);
        LOG.info("Total Rows：{}", pageInfo.getTotal());
        LOG.info("Total Pages：{}", pageInfo.getPages());

        List<BloguserQueryResp> list = CopyUtil.copyList(blogusersList, BloguserQueryResp.class);

        PageResp<BloguserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;

    }


    public void save(BloguserSaveReq req){
        Bloguser bloguser = CopyUtil.copy(req,Bloguser.class);
        if (ObjectUtils.isEmpty(req.getId())){
            // insert
            bloguser.setId(snowFlake.nextId());
            bloguserMapper.insert(bloguser);
        }else {
            //update
            bloguserMapper.updateByPrimaryKey(bloguser);
        }

    }

    public void delete(Long id){
        bloguserMapper.deleteByPrimaryKey(id);
    }


    public List<BloguserQueryResp> all() {
        BloguserExample bloguserExample = new BloguserExample();
        bloguserExample.setOrderByClause("sort asc");
        List<Bloguser> bloguserList = bloguserMapper.selectByExample(bloguserExample);

        // copy
        List<BloguserQueryResp> list = CopyUtil.copyList(bloguserList, BloguserQueryResp.class);

        return list;
    }
}
