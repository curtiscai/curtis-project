package com.curtis.dspool.hikaricp.raw.jdbc;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class JdbcSimpleTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcSimpleTest.class);

    @Test
    public void testJdbcWithInsert() {

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
        try {
            //2. 获得数据库连接
            Connection connection = DriverManager.getConnection(url, user, password);

            String insertSql = "INSERT INTO tb_user\n" +
                    "    (id, name, sex, birth, phone) \n" +
                    "VALUES \n" +
                    "    (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setLong(1, 1);
            preparedStatement.setString(2, "curtis1");
            preparedStatement.setBoolean(3, true);
            preparedStatement.setDate(4, Date.valueOf("1991-01-01"));
            preparedStatement.setLong(5, 18310001000L);

            int insertResult = preparedStatement.executeUpdate();
            LOGGER.info("insertResult = {}", insertResult);

            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testJdbcWithSelect() {
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
        try {
            //2. 获得数据库连接
            Connection connection = DriverManager.getConnection(url, user, password);

            String selectSql = "SELECT id, name, sex, birth, phone \n" +
                    "FROM tb_user \n" +
                    "WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, 1);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Boolean sex = resultSet.getBoolean("sex");
                Date birth = resultSet.getDate("birth");
                Long phone = resultSet.getLong("phone");
                LOGGER.info("id={}, name={}, sex={}, birth={}, phone={}", id, name, sex, birth, phone);
            }
            resultSet.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testJdbcWithUpdate() {
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
        try {
            //2. 获得数据库连接
            Connection connection = DriverManager.getConnection(url, user, password);

            String updateSql = "UPDATE tb_user\n" +
                    "SET name=?,sex=?,birth=?,phone=?\n" +
                    "WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setString(1, "curtis2");
            preparedStatement.setBoolean(2, false);
            preparedStatement.setDate(3, Date.valueOf("1991-01-02"));
            preparedStatement.setLong(4, 18310001002L);
            preparedStatement.setInt(5, 1);

            int updateResult = preparedStatement.executeUpdate();
            LOGGER.info("updateResult = {}", updateResult);

            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testJdbcWithDelete() {
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
        try {
            //2. 获得数据库连接
            Connection connection = DriverManager.getConnection(url, user, password);

            String deleteSql = "DELETE FROM tb_user\n" +
                    "WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);
            preparedStatement.setInt(1, 1);

            int deleteResult = preparedStatement.executeUpdate();
            LOGGER.info("deleteResult = {}", deleteResult);

            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
