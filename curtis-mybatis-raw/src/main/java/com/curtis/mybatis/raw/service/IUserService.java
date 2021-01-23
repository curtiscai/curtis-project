package com.curtis.mybatis.raw.service;


import com.curtis.mybatis.raw.entity.User;

import java.util.List;

/**
 * @author curtis
 * @desc 用户服务接口
 * @date 2020-09-03
 * @email 397773935@qq.com
 * @reference
 */
public interface IUserService {

    List<User> getUsers(Integer age, String sex);
}
