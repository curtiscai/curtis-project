package com.curtis.sharding.raw.mapper;

import com.curtis.sharding.raw.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author curtis
 * @desc 用户数据访问接口
 * @date 2020-09-03
 * @email 397773935@qq.com
 * @reference
 */
// Spring会扫描@Mapper注解的接口，也可以在启动类或者配置类上使用@MapperScan注解
//@Mapper
@Repository
public interface UserMapper {

    /**
     * 获取指定条件用户信息
     * @param age
     * @param sex
     * @return
     */
    List<User> getUsers(@Param("age") Integer age, @Param("sex") String sex);
}
