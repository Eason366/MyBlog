package com.eason.blog.controller;


import com.eason.blog.req.BlogQueryReq;
import com.eason.blog.req.BlogSaveReq;
import com.eason.blog.resp.BlogQueryResp;
import com.eason.blog.resp.CommonResp;
import com.eason.blog.resp.PageResp;
import com.eason.blog.service.BlogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @GetMapping("/list")
    public CommonResp list(@Valid BlogQueryReq req) {
        CommonResp<PageResp<BlogQueryResp>> resp = new CommonResp<>();
        PageResp<BlogQueryResp> list =  blogService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid BlogSaveReq req) {
        CommonResp resp = new CommonResp<>();
        blogService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        blogService.delete(id);
        return resp;
    }

    @GetMapping("/list/{id}")
    public CommonResp list(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        BlogQueryResp list =  blogService.list(id);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/find-mdContent/{id}")
    public CommonResp findmdContent(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content =  blogService.findmdContent(id);
        resp.setContent(content);
        return resp;
    }

    @GetMapping("/find-htmlContent/{id}")
    public CommonResp findhtmlContent(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content =  blogService.findhtmlContent(id);
        resp.setContent(content);
        return resp;
    }

}
