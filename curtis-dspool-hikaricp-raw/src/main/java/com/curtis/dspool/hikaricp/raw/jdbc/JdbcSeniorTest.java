package com.curtis.dspool.hikaricp.raw.jdbc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JdbcSeniorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSeniorTest.class);

    @Test
    public void testJdbcWithTransaction() {

        //1.加载驱动程序到JVM中
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:mysql://192.168.2.100:3306/db_test?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "000000";

        //2. 获得数据库连接
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);

            String selectSql = "INSERT INTO tb_user\n" +
                    "    (id, name, sex, birth, phone) \n" +
                    "VALUES \n" +
                    "    (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            int dataSize = 1000;
            Savepoint halfPoint = null;
            for (int i = 1; i <= dataSize; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "curtis" + i);
                preparedStatement.setBoolean(3, i % 2 == 1 ? true : false);
                preparedStatement.setDate(4, Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)));
                preparedStatement.setLong(5, 18310001000L + (i % 9 == 0 ? 9 : i % 9));
                int insertResult = preparedStatement.executeUpdate();
                // LOGGER.info("insertResult = {}", insertResult);
                if (i == dataSize / 2) {
                    halfPoint = connection.setSavepoint("half point");
                }
            }

            // 回滚到提交一半的点
            connection.rollback(halfPoint);
            // 设置不自动提交后，如果不手动commit，则数据不会入库
            connection.commit();
            // connection.rollback();

            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Test
    public void testJdbcWithBatch() {

        //1.加载驱动程序到JVM中
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:mysql://192.168.2.100:3306/db_test?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "000000";

        //2. 获得数据库连接
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            String insertSql = "INSERT INTO tb_user\n" +
                    "    (id, name, sex, birth, phone) \n" +
                    "VALUES \n" +
                    "    (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);

            int dataSize = 1000;
            for (int i = 1; i <= dataSize; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "curtis" + i);
                preparedStatement.setBoolean(3, i % 2 == 1 ? true : false);
                preparedStatement.setDate(4, Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)));
                preparedStatement.setLong(5, 18310001000L + (i % 9 == 0 ? 9 : i % 9));
                // int insertResult = preparedStatement.executeUpdate();
                // LOGGER.info("insertResult = {}", insertResult);
                preparedStatement.addBatch();
                if (i % 100 == 0) {
                    int[] ints = preparedStatement.executeBatch();
                    System.out.println(ints);
                    preparedStatement.clearBatch();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
