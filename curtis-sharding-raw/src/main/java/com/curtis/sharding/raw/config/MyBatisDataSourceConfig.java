package com.curtis.sharding.raw.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author curtis
 * @desc
 * @date 2020-09-04
 * @email 397773935@qq.com
 * @reference
 */
//@EnableTransactionManagement
@MapperScan(basePackages = "com.curtis.sharding.raw.**.mapper")
@Configuration
public class MyBatisDataSourceConfig {

//    @Bean(value = "userDataSource")
//    public DataSource userDataSource() {
//        String driverClassName = "com.mysql.cj.jdbc.Driver";
//        String jdbcUrl = "jdbc:mysql://localhost:3306/db_user?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";
//        String userName = "root";
//        String password = "root";
//        return this.createDataSource(driverClassName, jdbcUrl, userName, password);
//    }
//
//    private DataSource createDataSource(String driverClassName, String jdbcUrl, String userName, String password) {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setJdbcUrl(jdbcUrl);
//        dataSource.setUsername(userName);
//        dataSource.setPassword(password);
//
//        // TODO 连接池其他配置
//        return dataSource;
//    }

    @Bean(name = "testSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver()
//                .getResource("classpath:mybatis/mybatis-config.xml"));
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mappers/**/*Mapper.xml"));
        sessionFactory.setTypeAliasesPackage("com.curtis.sharding.raw.**.entity");
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        // 还可以设置其他属性
        sessionFactory.setConfiguration(configuration);
        return sessionFactory.getObject();
    }

    @Bean(name = "testDataSourceTransactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("testDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "testSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
