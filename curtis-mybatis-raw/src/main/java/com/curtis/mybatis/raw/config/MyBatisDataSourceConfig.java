package com.curtis.mybatis.raw.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author curtis
 * @desc
 * @date 2020-09-04
 * @email 397773935@qq.com
 * @reference
 */
//@EnableTransactionManagement
@MapperScan(basePackages = "com.curtis.mybatis.javaconfig.mapper")
@Configuration
public class MyBatisDataSourceConfig {

    @Bean(value = "userDataSource")
    public DataSource userDataSource() {
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://localhost:3306/db_user?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";
        String userName = "root";
        String password = "root";
        return this.createDataSource(driverClassName, jdbcUrl, userName, password);
    }

    private DataSource createDataSource(String driverClassName, String jdbcUrl, String userName, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        // TODO 连接池其他配置
        return dataSource;
    }

    @Bean(name = "userSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("userDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver()
//                .getResource("classpath:mybatis/mybatis-config.xml"));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mappers/**/*Mapper.xml"));
        sessionFactory.setTypeAliasesPackage("com.curtis.mybatis.javaconfig.entity");
        return sessionFactory.getObject();
    }

    @Bean(name = "userDataSourceTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("userDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "userSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("userSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
