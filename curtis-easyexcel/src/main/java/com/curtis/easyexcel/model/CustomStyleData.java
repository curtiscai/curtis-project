package com.curtis.easyexcel.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.*;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author curtis
 * @desc 自定义数据以及单元格格式
 * 常见样式：
 * HeadStyle、ContentStyle 设置标题和内容样式
 * HeadFontStyle、ContentFontStyle 设置标题和内容字体样式
 * HeadRowHeight、ContentRowHeight、ColumnWidth 设置标题和内容行高以及列宽
 * @date 2020-06-27
 * @email 397773935@qq.com
 * @reference
 */
@HeadFontStyle(fontHeightInPoints = 11) // 设置标题字体样式
@HeadRowHeight(15) // 设置标题行高，-1默认
@HeadStyle(horizontalAlignment = HorizontalAlignment.CENTER
        , fillBackgroundColor = -1) // 设置标题样式，比如horizontalAlignment默认就是居中
@ContentRowHeight(value = -1) // 设置内容行高，-1默认
public class CustomStyleData {

    @ColumnWidth(10) // 设置列宽，可以在类上设置则对所有列生效
    @ContentFontStyle(fontName = "Calibri", fontHeightInPoints = 11, color = Font.COLOR_NORMAL)
    @ContentStyle(horizontalAlignment = HorizontalAlignment.CENTER,
            verticalAlignment = VerticalAlignment.CENTER,
            fillBackgroundColor = -1) // 设置背景色，水平对齐和垂直对齐样式
    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("年龄")
    private Integer age;

    @NumberFormat("#.00")
    @ExcelProperty("身高")
    private BigDecimal height;

    @ColumnWidth(10)
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
