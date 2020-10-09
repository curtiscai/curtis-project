package com.curtis.redis.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author curtis
 * @desc 用户实体模型类
 * @date 2020-06-25
 * @email 397773935@qq.com
 * @reference
 */
public class UserEntity implements Serializable {

    private Integer id;

    private String name;

    private Integer age;

    private BigDecimal height;

    private Date birth;

    private String sex;

    private String address;

    public UserEntity() {
    }

    public UserEntity(Integer id, String name, Integer age, BigDecimal height, Date birth, String sex, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.height = height;
        this.birth = birth;
        this.sex = sex;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", birth=" + birth +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
