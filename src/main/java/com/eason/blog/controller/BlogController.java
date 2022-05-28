package com.eason.blog.controller;


import com.eason.blog.req.BlogReq;
import com.eason.blog.resp.BlogResp;
import com.eason.blog.resp.CommonResp;
import com.eason.blog.resp.PageResp;
import com.eason.blog.service.BlogService;
import com.eason.blog.domain.Blog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @GetMapping("/list")
    public CommonResp list(BlogReq req) {
        CommonResp<PageResp<BlogResp>> resp = new CommonResp<>();
        PageResp<BlogResp> list =  blogService.list(req);
        resp.setContent(list);
        return resp;
    }

}
