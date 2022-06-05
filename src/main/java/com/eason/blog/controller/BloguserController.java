package com.eason.blog.controller;


import com.alibaba.fastjson.JSONObject;
import com.eason.blog.req.BloguserLoginReq;
import com.eason.blog.req.BloguserQueryReq;
import com.eason.blog.req.BloguserSaveReq;
import com.eason.blog.resp.BloguserLoginResp;
import com.eason.blog.resp.BloguserQueryResp;
import com.eason.blog.resp.CommonResp;
import com.eason.blog.resp.PageResp;
import com.eason.blog.service.BloguserService;
import com.eason.blog.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class BloguserController {
    private static final Logger LOG = LoggerFactory.getLogger(BloguserController.class);

    @Resource
    private BloguserService bloguserService;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisTemplate redisTemplate;



    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<BloguserQueryResp>> resp = new CommonResp<>();
        List<BloguserQueryResp> list = bloguserService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid BloguserQueryReq req) {
        CommonResp<PageResp<BloguserQueryResp>> resp = new CommonResp<>();
        PageResp<BloguserQueryResp> list =  bloguserService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid BloguserSaveReq req) {
        CommonResp resp = new CommonResp<>();
        bloguserService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        bloguserService.delete(id);
        return resp;
    }

    @PostMapping("/Login")
    public CommonResp Login(@Valid @RequestBody BloguserLoginReq req) {
        CommonResp<BloguserLoginResp> resp = new CommonResp<>();
        BloguserLoginResp bloguserLoginResp = bloguserService.login(req);

        Long token = snowFlake.nextId();
        LOG.info("Generate token：{}，put in to redis", token);
        bloguserLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(bloguserLoginResp), 3600 * 24, TimeUnit.SECONDS);


        resp.setContent(bloguserLoginResp);
        return resp;
    }

}
