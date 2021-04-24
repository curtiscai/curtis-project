package com.curtis.dspool.hikaricp.jdbc;

import com.curtis.dspool.hikaricp.entity.User;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.awt.print.Book;
import java.sql.*;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JdbcTemplateSimpleTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplateSimpleTest.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Test
    public void testInsert() {
        String insertSql = "INSERT INTO tb_user\n" +
                "    (id, name, sex, birth, phone) \n" +
                "VALUES \n" +
                "    (?,?,?,?,?);";

        for (int i = 1; i <= 10000; i++) {
            User user = new User(Long.valueOf(i), "curtis" + i, i % 2 == 1 ? true : false,
                    Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)), 18310001000L + (i % 9 == 0 ? 9 : i % 9));
            int update = jdbcTemplate.update(insertSql, user.getId(), user.getName(), user.getSex(), user.getBirth(), user.getPhone());

        }
    }

    @Test
    public void testSelect() {
        String selectSql = "SELECT id, name, sex, birth, phone \n" +
                "FROM tb_user \n" +
                "WHERE birth = ?";

        List<User> userList = jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<>(User.class), "1991-01-01");
        System.out.println(userList);

        String selectSql2 = "SELECT id, name, sex, birth, phone \n" +
                "FROM tb_user \n" +
                "WHERE id = ?";
        User user = jdbcTemplate.queryForObject(selectSql2, new BeanPropertyRowMapper<>(User.class), 1);
        System.out.println(user);
    }

    @Test
    public void testUpdate() {
        String updateSql = "UPDATE tb_user\n" +
                "SET name=?,sex=?,birth=?,phone=?\n" +
                "WHERE id=?";

        int updateResult = jdbcTemplate.update(updateSql, "curtis2", false, "1991-01-02", 18310001002L, 1);
        System.out.println(updateResult);
    }

    @Test
    public void testDelete() {
        String deleteSql = "DELETE FROM tb_user\n" +
                "WHERE id=?";

        int deleteResult = jdbcTemplate.update(deleteSql, 1);
        System.out.println(deleteResult);
    }

    @Test
    public void testJdbcWithTransaction() {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        String insertSql = "INSERT INTO tb_user\n" +
                "    (id, name, sex, birth, phone) \n" +
                "VALUES \n" +
                "    (?,?,?,?,?);";

        List<User> userList = Lists.newArrayList();
        for (int i = 1; i <= 10000; i++) {
            User user = new User((long) i, "curtis" + i, i % 2 == 1 ? true : false,
                    Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)), 18310001000L + (i % 9 == 0 ? 9 : i % 9));
            userList.add(user);
        }

        jdbcTemplate.batchUpdate(insertSql, userList, 1000, (ps, argument) -> {
            ps.setLong(1, argument.getId());
            ps.setString(2, argument.getName());
            ps.setBoolean(3, argument.getSex());
            ps.setDate(4, argument.getBirth());
            ps.setLong(5, argument.getPhone());
        });

        // 不手动提交数据不会入库
        dataSourceTransactionManager.commit(transactionStatus);
    }

    @Test
    public void testJdbcWithBatch() {

        String insertSql = "INSERT INTO tb_user\n" +
                "    (id, name, sex, birth, phone) \n" +
                "VALUES \n" +
                "    (?,?,?,?,?);";

        List<User> userList = Lists.newArrayList();
        for (int i = 1; i <= 10000; i++) {
            User user = new User((long) i, "curtis" + i, i % 2 == 1 ? true : false,
                    Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)), 18310001000L + (i % 9 == 0 ? 9 : i % 9));
            userList.add(user);
        }

        jdbcTemplate.batchUpdate(insertSql, userList, 1000, (ps, argument) -> {
            ps.setLong(1, argument.getId());
            ps.setString(2, argument.getName());
            ps.setBoolean(3, argument.getSex());
            ps.setDate(4, argument.getBirth());
            ps.setLong(5, argument.getPhone());
        });
    }

}
