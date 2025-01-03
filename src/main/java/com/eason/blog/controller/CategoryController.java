package com.eason.blog.controller;


import com.eason.blog.req.CategoryQueryReq;
import com.eason.blog.req.CategorySaveReq;
import com.eason.blog.resp.BlogQueryResp;
import com.eason.blog.resp.CategoryQueryResp;
import com.eason.blog.resp.CommonResp;
import com.eason.blog.resp.PageResp;
import com.eason.blog.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list =  categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }

    @DeleteMapping("/deleteIDs/{idsStr}")
    public CommonResp deleteIDs(@PathVariable String idsStr) {
        List<String> list = Arrays.asList(idsStr.split(","));
        CommonResp resp = new CommonResp<>();
        categoryService.delete(list);
        return resp;
    }


}
