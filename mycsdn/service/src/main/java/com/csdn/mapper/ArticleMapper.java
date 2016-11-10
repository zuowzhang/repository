package com.csdn.mapper;

import com.csdn.model.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zbin on 16-11-9.
 */
@Repository("articleMapper")
public interface ArticleMapper {
    Article getById(@Param("aid") int aid);
    List<Article> getByUserId(@Param("uid") int uid);
}
