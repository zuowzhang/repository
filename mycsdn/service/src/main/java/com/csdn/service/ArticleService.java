package com.csdn.service;

import com.csdn.model.Article;

import java.util.List;

/**
 * Created by zbin on 16-11-9.
 */
public interface ArticleService {
    Article getById(int aid);
    List<Article> getByUserId(int uid);
}
