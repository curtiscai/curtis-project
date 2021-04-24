package com.curtis.dspool.hikaricp.raw.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.*;

public class PerformanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceTest.class);

    /**
     * 单连接，数据单条插入
     * speed time: 60 s
     */
    @Test
    public void testInsertWithSingle() {
        LocalTime startTime = LocalTime.now();
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

            int dataSize = 100000;
            for (int i = 1; i <= dataSize; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "curtis" + i);
                preparedStatement.setBoolean(3, i % 2 == 1 ? true : false);
                preparedStatement.setDate(4, Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)));
                preparedStatement.setLong(5, 18310001000L + (i % 9 == 0 ? 9 : i % 9));
                int insertResult = preparedStatement.executeUpdate();
                // LOGGER.info("insertResult = {}", insertResult);
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

        LocalTime endTime = LocalTime.now();
        // speed time: 60 s
        LOGGER.info("speed time: {} s", Duration.between(startTime, endTime).getSeconds());
    }

    /**
     * 单连接，数据批量插入
     * speed time: 60 S
     */
    @Test
    public void testInsertWithBatch() {
        LocalTime startTime = LocalTime.now();

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

            int dataSize = 100000;
            for (int i = 1; i <= dataSize; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "curtis" + i);
                preparedStatement.setBoolean(3, i % 2 == 1 ? true : false);
                preparedStatement.setDate(4, Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)));
                preparedStatement.setLong(5, 18310001000L + (i % 9 == 0 ? 9 : i % 9));
                // int insertResult = preparedStatement.executeUpdate();
                // LOGGER.info("insertResult = {}", insertResult);
                preparedStatement.addBatch();
                if (i % 1000 == 0) {
                    int[] ints = preparedStatement.executeBatch();
                    long sum = Arrays.stream(ints).asLongStream().filter(item -> item == 1).sum();
                    LOGGER.info("Successfully insert {} in batches!", sum);

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
        LocalTime endTime = LocalTime.now();
        // speed time: 60 s
        LOGGER.info("speed time: {} s", Duration.between(startTime, endTime).getSeconds());
    }

    /**
     * speed time: 48 s
     */
    @Test
    public void testInsertOfMultiConnWithBatch() {
        LocalTime startTime = LocalTime.now();

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
        ExecutorService service = new ThreadPoolExecutor(10, 40, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        //2. 获得数据库连接
        try {
            int dataSize = 10000;
            for (int i = 1; i <= dataSize; i++) {
                Connection connection = DriverManager.getConnection(url, user, password);
                String insertSql = "INSERT INTO tb_user\n" +
                        "    (id, name, sex, birth, phone) \n" +
                        "VALUES \n" +
                        "    (?,?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "curtis" + i);
                preparedStatement.setBoolean(3, i % 2 == 1 ? true : false);
                preparedStatement.setDate(4, Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)));
                preparedStatement.setLong(5, 18310001000L + (i % 9 == 0 ? 9 : i % 9));
                int insertResult = preparedStatement.executeUpdate();
                // LOGGER.info("insertResult = {}", insertResult);
                preparedStatement.close();
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        LocalTime endTime = LocalTime.now();
        // speed time: 60 s
        LOGGER.info("speed time: {} s", Duration.between(startTime, endTime).getSeconds());
    }

    /**
     * speed time: PT6.384S
     */
    @Test
    public void test() {

        LocalTime startTime = LocalTime.now();
        try (InputStream inputStream = HikariDataSourceTest.class.getClassLoader().getResourceAsStream("hikari.properties")) {
            // 加载属性文件并解析
            Properties properties = new Properties();
            properties.load(inputStream);
            HikariConfig hikariConfig = new HikariConfig(properties);
            HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
            // hikariDataSource.addDataSourceProperty();

            int dataSize = 10000;
            for (int i = 1; i <= dataSize; i++) {
                Connection connection = hikariDataSource.getConnection();
                String insertSql = "INSERT INTO tb_user\n" +
                        "    (id, name, sex, birth, phone) \n" +
                        "VALUES \n" +
                        "    (?,?,?,?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "curtis" + i);
                preparedStatement.setBoolean(3, i % 2 == 1 ? true : false);
                preparedStatement.setDate(4, Date.valueOf("1991-01-0" + (i % 9 == 0 ? 9 : i % 9)));
                preparedStatement.setLong(5, 18310001000L + (i % 9 == 0 ? 9 : i % 9));
                int insertResult = preparedStatement.executeUpdate();
                // LOGGER.info("insertResult = {}", insertResult);
                preparedStatement.close();
                connection.close();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        LocalTime endTime = LocalTime.now();
        // speed time: PT6.397S
        LOGGER.info("speed time: {}", Duration.between(startTime, endTime));
    }
}
