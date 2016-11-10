package com.csdn.service.impl;

import com.csdn.mapper.ArticleMapper;
import com.csdn.model.Article;
import com.csdn.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zbin on 16-11-9.
 */
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    public Article getById(int aid) {
        return articleMapper.getById(aid);
    }

    public List<Article> getByUserId(int uid) {
        return articleMapper.getByUserId(uid);
    }
}
