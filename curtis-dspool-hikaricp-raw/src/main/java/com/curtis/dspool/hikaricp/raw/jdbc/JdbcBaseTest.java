package com.curtis.dspool.hikaricp.raw.jdbc;

import org.junit.Test;

import java.sql.*;

public class JdbcBaseTest {

    @Test
    public void testJdbc() {

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
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id,name,birth FROM tb_user");
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String birth = resultSet.getString("birth");
                System.out.println(id);
                System.out.println(name);
                System.out.println(birth);
            }

            System.out.println(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
