package com.eason.blog.service;

import com.eason.blog.domain.Category;
import com.eason.blog.domain.CategoryExample;
import com.eason.blog.mapper.CategoryMapper;
import com.eason.blog.req.CategoryQueryReq;
import com.eason.blog.req.CategorySaveReq;
import com.eason.blog.resp.CategoryQueryResp;
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
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categorysList = categoryMapper.selectByExample(categoryExample);


        PageInfo<Category> pageInfo = new PageInfo<>(categorysList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<CategoryQueryResp> list = CopyUtil.copyList(categorysList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;

    }


    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req,Category.class);
        if (ObjectUtils.isEmpty(req.getId())){
            // insert
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else {
            //update
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }

}
