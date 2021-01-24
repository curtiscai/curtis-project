package com.curtis.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author curtis
 * @desc
 * @date 2021-01-23
 * @email 397773935@qq.com
 * @reference
 */
public class Teacher {

    @JsonIgnore
    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    private Long phone;

    private BigDecimal height;

    public Teacher() {
    }

    public Teacher(Long id, String name, Date birth, Long phone, BigDecimal height) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }
}
