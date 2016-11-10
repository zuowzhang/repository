package com.csdn.controller;

import com.csdn.model.Article;
import com.csdn.model.User;
import com.csdn.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zbin on 16-11-9.
 */
@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @RequestMapping(value = "/get")
    @ResponseBody
    public Article getById(int aid) {
        return articleService.getById(aid);
    }

    @RequestMapping(value = "/from")
    @ResponseBody
    public List<Article> fromUid(int uid) {
        return articleService.getByUserId(uid);
    }

}
