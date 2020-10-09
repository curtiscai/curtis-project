package com.curtis.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.curtis.easyexcel.model.DemoData;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author curtis
 * @desc 使用EasyExcel写入数据测试类
 * @date 2020-06-25
 * @email 397773935@qq.com
 * @reference
 */
public class SimpleWriteTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleWriteTest.class);

    /**
     * 数据写入Excel测试-使用Model：最简单的写入Excel方式一
     * 20:11:32.929 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：6秒
     * 20:12:03.754 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：5秒
     * 20:12:27.143 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：6秒
     */
    @Test
    public void testWriteWithModel1() {
        // 文件路径为curtis-project/curtis-easyexcel/target/test-classes/用户-HHmmss.xlsx
        String path = SimpleWriteTest.class.getResource("/").getPath();
        String hourMinuteStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String fileName = path + "用户-" + hourMinuteStr + ".xlsx";
        LOGGER.info("excel文件路径为：{}", fileName);

        LocalTime startTime = LocalTime.now();
        List<DemoData> datas = this.getDemoDatas(1_0000);
        /**
         * public static ExcelWriterBuilder write(String pathName, Class head)中head是标题使用的Class
         * sheet(String sheetName)方法用于指定写入sheet的名称，不指定sheetNo则默认写入sheet0
         * doWrite(List data)方法内部调用了excelWriter.finish()方法所以不需要关闭流
         */
        // 这种写入Excel的思路是先调用EasyExcel的静态write(String pathName, Class head)方法指定文件路径和标题信息(返回ExcelWriterBuilder)
        // 然后调用sheet(String sheetName)方法指定sheet页的标号和名字等信息(返回ExcelWriterSheetBuilder)
        // 最后调用doWrite(List data)方法写入数据并关闭流。
        EasyExcel.write(fileName, DemoData.class).sheet("用户信息").doWrite(datas);
        LocalTime endTime = LocalTime.now();
        Duration between = Duration.between(startTime, endTime);
        LOGGER.info("写入Excel耗时：{}秒", between.getSeconds());
    }

    /**
     * 数据写入Excel测试-使用Model：最简单的写入Excel方式二
     * 20:12:53.935 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：5秒
     * 20:13:12.090 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：6秒
     * 20:13:33.806 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：4秒
     */
    @Test
    public void testWriteWithModel2() {
        // 文件路径为curtis-project/curtis-easyexcel/target/test-classes/用户-HHmmss.xlsx
        String path = SimpleWriteTest.class.getResource("/").getPath();
        String hourMinuteStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String fileName = path + "用户-" + hourMinuteStr + ".xlsx";
        LOGGER.info("excel文件路径为：{}", fileName);

        LocalTime startTime = LocalTime.now();
        List<DemoData> datas = this.getDemoDatas(1_0000);
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
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
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

    private List<DemoData> getDemoDatas(int count) {
        List<DemoData> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DemoData demoData = new DemoData();
            datas.add(demoData);
            demoData.setName("curtis" + (i + 1));
            demoData.setAge(20 + (i % 10 + 1));
            demoData.setHeight(BigDecimal.valueOf(180 + (i % 10 + 1)));
            demoData.setBirth("1990-01-" + ((i % 30) < 9 ? ("0" + (i % 30 + 1)) : (i % 30 + 1)));
            demoData.setSex(i % 2 == 0 ? "男" : "女");
        }
        return datas;
    }


    /***************************************************************************************************************/

    /**
     * 数据写入Excel测试-不使用Model
     * 22:07:20.997 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：5秒
     * 22:10:29.170 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：7秒
     * 22:10:55.634 [main] INFO com.curtis.easyexcel.write.SimpleWriteTest - 写入Excel耗时：6秒
     */
    @Test
    public void testWriteWithNoModel() {
        // 文件路径为curtis-project/curtis-easyexcel/target/test-classes/用户-HHmmss.xlsx
        String path = SimpleWriteTest.class.getResource("/").getPath();
        String hourMinuteStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String fileName = path + "用户-" + hourMinuteStr + ".xlsx";
        LOGGER.info("excel文件路径为：{}", fileName);

        LocalTime startTime = LocalTime.now();
        List<List<String>> head = this.getHead();
        List<List<Object>> datas = this.getDatas(1_0000);
        /**
         * public static ExcelWriterBuilder write(String pathName)方法用于指定文件
         * sheet(String sheetName)方法用于指定写入sheet的名称，不指定sheetNo则默认写入sheet0
         * head(List<List<String>> head)方法用于指定标题
         * doWrite(List data)方法内部调用了excelWriter.finish()方法所以不需要关闭流
         */
        // 这种写入Excel的思路是先调用EasyExcel的静态write(String pathName)方法指定文件路径和标题信息(返回ExcelWriterBuilder)
        // 然后调用sheet(String sheetName)方法指定sheet页的标号和名字等信息(返回ExcelWriterSheetBuilder)
        // 最后调用doWrite(List data)方法写入数据并关闭流。
//        EasyExcel.write(fileName, DemoData.class).sheet("用户信息").doWrite(datas);
        EasyExcel.write(fileName).sheet("用户信息").head(head).doWrite(datas);
        LocalTime endTime = LocalTime.now();
        Duration between = Duration.between(startTime, endTime);
        LOGGER.info("写入Excel耗时：{}秒", between.getSeconds());
    }

    private List<List<String>> getHead() {
        List<List<String>> headList = Lists.newArrayList();
        headList.add(Lists.newArrayList("姓名"));
        headList.add(Lists.newArrayList("年龄"));
        headList.add(Lists.newArrayList("身高"));
        headList.add(Lists.newArrayList("出生年月"));
        headList.add(Lists.newArrayList("性别"));
        return headList;
    }

    private List<List<Object>> getDatas(int count) {
        List<List<Object>> datas = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            List<Object> data = Lists.newArrayList();
            datas.add(data);
            data.add("curtis" + (i + 1));
            data.add(20 + (i % 10 + 1));
            data.add(BigDecimal.valueOf(180 + (i % 10 + 1)));
            data.add("1990-01-" + ((i % 30) < 9 ? ("0" + (i % 30 + 1)) : (i % 30 + 1)));
            data.add(i % 2 == 0 ? "男" : "女");
        }
        return datas;
    }
}
