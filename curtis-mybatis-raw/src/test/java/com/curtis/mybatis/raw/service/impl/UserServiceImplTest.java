package com.curtis.mybatis.raw.service.impl;

import com.curtis.mybatis.raw.CurtisMybatisRawApplication;
import com.curtis.mybatis.raw.entity.User;
import com.curtis.mybatis.raw.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author curtis
 * @desc
 * @date 2020-09-03
 * @email 397773935@qq.com
 * @reference
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CurtisMybatisRawApplication.class)
public class UserServiceImplTest {

    @Autowired
    private IUserService iUserService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

    @Test
    public void testGetUsers(){
        List<User> users = iUserService.getUsers(null, null);
        LOGGER.info(users.toString());
    }
}
