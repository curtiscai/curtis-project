package com.curtis.dspool.hikaricp.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Properties;

public class HikariDataSourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HikariDataSourceTest.class);


    @Test
    public void test() {

        LocalTime startTime = LocalTime.now();
        try (InputStream inputStream = HikariDataSourceTest.class.getClassLoader().getResourceAsStream("hikari.properties")) {
            // 加载属性文件并解析：
            Properties properties = new Properties();
            properties.load(inputStream);
            HikariConfig config = new HikariConfig(properties);
            HikariDataSource hikariDataSource = new HikariDataSource(config);
            Connection connection = hikariDataSource.getConnection();

            String insertSql = "INSERT INTO tb_user\n" +
                    "    (id, name, sex, birth, phone) \n" +
                    "VALUES \n" +
                    "    (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);

            int dataSize = 10000;
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
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        LocalTime endTime = LocalTime.now();
        // speed time: PT6.397S
        LOGGER.info("speed time: {}", Duration.between(startTime, endTime));
    }
}
