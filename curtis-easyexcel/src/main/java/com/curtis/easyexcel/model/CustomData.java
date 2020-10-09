package com.curtis.easyexcel.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author curtis
 * @desc 自定义数据格式
 * @date 2020-06-27
 * @email 397773935@qq.com
 * @reference
 */
public class CustomData {
    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @NumberFormat("#.00米")
    @ExcelProperty("身高")
    private BigDecimal height;

    @DateTimeFormat("yyyy/MM/dd")
    @ExcelProperty("出生年月")
    private Date birth;

    @ExcelProperty("性别")
    private String sex;

    @ExcelIgnore
    private String address;

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
}
