package com.curtis.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.curtis.easyexcel.model.CustomData;
import com.curtis.easyexcel.model.CustomStyleData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author curtis
 * @desc 使用EasyExcel自定义单元格(标题 / 内容)格式
 * @date 2020-06-27
 * @email 397773935@qq.com
 * @reference
 */
public class CustomStyleWriteTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomStyleWriteTest.class);

    /**
     * EasyExcel的com.alibaba.excel.annotation.write.style包提供很多注解用于自定义标题和内容的单元格的样式
     * 常见样式：
     * HeadStyle、ContentStyle 设置标题和内容样式
     * HeadFontStyle、ContentFontStyle 设置标题和内容字体样式
     * HeadRowHeight、ContentRowHeight、ColumnWidth 设置标题和内容行高以及列宽
     * 22:37:45.834 [main] INFO com.curtis.easyexcel.write.CustomWriteTest - 写入Excel耗时：7秒
     * 22:38:24.228 [main] INFO com.curtis.easyexcel.write.CustomWriteTest - 写入Excel耗时：6秒
     * 22:39:03.386 [main] INFO com.curtis.easyexcel.write.CustomWriteTest - 写入Excel耗时：7秒
     */
    @Test
    public void testCustomStyleWrite() {
        // 文件路径为curtis-project/curtis-easyexcel/target/test-classes/用户-HHmmss.xlsx
        String path = SimpleWriteTest.class.getResource("/").getPath();
        String hourMinuteStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String fileName = path + "用户-" + hourMinuteStr + ".xlsx";
        LOGGER.info("excel文件路径为：{}", fileName);

        LocalTime startTime = LocalTime.now();
        List<CustomStyleData> datas = this.getCustomStypeDatas(1_0000);
        /**
         * write(String pathName, Class head)中head是标题使用的Class
         * writerSheet(String sheetName)方法用于指定写入sheet的名称，不指定sheetNo则默认写入sheet0
         * write(List data, WriteSheet writeSheet)方法内部调用了excelWriter.finish()方法所以不需要关闭流
         */
        // 这种写入Excel的思路是先调用EasyExcel的静态write(String pathName, Class head)方法指定文件路径和标题信息，
        // 返回ExcelWriterBuilder对象，再调用build()方法来构建ExcelWriter对象。
        // 然后调用EasyExcel的静态writerSheet(String sheetName)方法指定sheet页的标号和名字等信息
        // 返回ExcelWriterSheetBuilder对象，再调用build()方法来构建WriterSheet
        // 最后调用ExcelWriter对象的write(List data, WriteSheet writeSheet)方法来指定数据和上步创建好的WriteSheet对象
        // 由于调用write方法后返回值还是这个ExcelWriter对象，所以直接链式调用finish()方法即可关闭流，也可以单独关闭。
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(fileName, CustomStyleData.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("用户信息").build();
            excelWriter.write(datas, writeSheet);
        } finally {
            if (excelWriter != null)
                excelWriter.finish(); // 需要手动关闭流
        }
//        EasyExcel.write(fileName, DemoData.class).sheet("用户信息").doWrite(datas);
        LocalTime endTime = LocalTime.now();
        Duration between = Duration.between(startTime, endTime);
        LOGGER.info("写入Excel耗时：{}秒", between.getSeconds());
    }

    private List<CustomStyleData> getCustomStypeDatas(int count) {
        List<CustomStyleData> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            CustomStyleData customStyleData = new CustomStyleData();
            datas.add(customStyleData);
            customStyleData.setName("curtis" + (i + 1));
            customStyleData.setAge(20 + (i % 10 + 1));
            customStyleData.setHeight(BigDecimal.valueOf(180 + (i % 10 + 1)));
            String birthStr = "1990-01-" + ((i % 30) < 9 ? ("0" + (i % 30 + 1)) : (i % 30 + 1)) + "T00:00:00Z";
            customStyleData.setBirth(Date.from(Instant.parse(birthStr)));
            customStyleData.setSex(i % 2 == 0 ? "男" : "女");
        }
        return datas;
    }
}
