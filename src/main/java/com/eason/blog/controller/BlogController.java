package com.eason.blog.controller;


import com.eason.blog.resp.CommonResp;
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
    public CommonResp list() {
        CommonResp<List<Blog>> resp = new CommonResp<>();
        List<Blog> list =  blogService.list();
        resp.setContent(list);
        return resp;
    }

}
