package com.csdn.mapper;

import com.csdn.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zbin on 16-11-9.
 */
@Repository("userMapper")
public interface UserMapper {
    User getById(@Param("uid") int uid);
}
