package com.eason.blog.controller;


import com.eason.blog.req.BlogQueryReq;
import com.eason.blog.req.BlogSaveReq;
import com.eason.blog.resp.BlogQueryResp;
import com.eason.blog.resp.CommonResp;
import com.eason.blog.resp.PageResp;
import com.eason.blog.service.BlogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @GetMapping("/list")
    public CommonResp list(BlogQueryReq req) {
        CommonResp<PageResp<BlogQueryResp>> resp = new CommonResp<>();
        PageResp<BlogQueryResp> list =  blogService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody BlogSaveReq req) {
        CommonResp resp = new CommonResp<>();
        blogService.save(req);
        return resp;
    }

}
