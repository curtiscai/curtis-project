package com.curtis.mybatis.service.impl;

import com.curtis.mybatis.entity.User;
import com.curtis.mybatis.mapper.UserMapper;
import com.curtis.mybatis.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author curtis
 * @desc 用户接口服务实现类
 * @date 2020-09-03
 * @email 397773935@qq.com
 * @reference
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUsers(Integer age, String sex) {
        List<User> users = userMapper.getUsers(age, sex);
        return users;
    }
}
