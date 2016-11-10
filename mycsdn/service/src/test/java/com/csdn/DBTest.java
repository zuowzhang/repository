package com.csdn;

import com.csdn.mapper.ArticleMapper;
import com.csdn.mapper.UserMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zbin on 16-11-9.
 */
public class DBTest {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("service-context.xml");
        UserMapper userMapper = ctx.getBean(UserMapper.class);
        ArticleMapper articleMapper = ctx.getBean(ArticleMapper.class);
        System.out.printf(userMapper.getById(1).toString());
        System.out.println(articleMapper.getByUserId(1));
    }
}
