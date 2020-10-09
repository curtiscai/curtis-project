package com.curtis.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.curtis.easyexcel.model.DemoData;
import com.curtis.easyexcel.model.DemoDataWith20;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author curtis
 * @desc EasyExcel写入数据性能测试
 * @date 2020-06-27
 * @email 397773935@qq.com
 * @reference
 */
public class PerformanceWriteTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceWriteTest.class);

    /**
     * 测试：写入100w条5列的数据
     * 11:03:00.516 [main] INFO com.curtis.easyexcel.write.PerformanceWriteTest - 写入Excel耗时：19秒
     * 11:04:36.469 [main] INFO com.curtis.easyexcel.write.PerformanceWriteTest - 写入Excel耗时：20秒
     * 11:05:13.761 [main] INFO com.curtis.easyexcel.write.PerformanceWriteTest - 写入Excel耗时：18秒
     */
    @Test
    public void testWrite100wWith5Field() {
        // 文件路径为curtis-project/curtis-easyexcel/target/test-classes/用户-HHmmss.xlsx
        String path = SimpleWriteTest.class.getResource("/").getPath();
        String hourMinuteStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String fileName = path + "用户-" + hourMinuteStr + ".xlsx";
        LOGGER.info("excel文件路径为：{}", fileName);

        LocalTime startTime = LocalTime.now();
        List<DemoData> datas = this.getDemoDatasWith5Field(100_0000);
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
     * 测试：写入100w条20列的数据
     * 11:29:02.151 [main] INFO com.curtis.easyexcel.write.PerformanceWriteTest - 写入Excel耗时：87秒
     * 11:30:58.799 [main] INFO com.curtis.easyexcel.write.PerformanceWriteTest - 写入Excel耗时：88秒
     * 11:35:21.208 [main] INFO com.curtis.easyexcel.write.PerformanceWriteTest - 写入Excel耗时：86秒
     */
    @Test
    public void testWrite100wWith20Field() {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 文件路径为curtis-project/curtis-easyexcel/target/test-classes/用户-HHmmss.xlsx
        String path = SimpleWriteTest.class.getResource("/").getPath();
        String hourMinuteStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String fileName = path + "用户-" + hourMinuteStr + ".xlsx";
        LOGGER.info("excel文件路径为：{}", fileName);

        LocalTime startTime = LocalTime.now();
        List<DemoDataWith20> datas = this.getDemoDatasWith20Field(100_0000);
        /**
         * public static ExcelWriterBuilder write(String pathName, Class head)中head是标题使用的Class
         * sheet(String sheetName)方法用于指定写入sheet的名称，不指定sheetNo则默认写入sheet0
         * doWrite(List data)方法内部调用了excelWriter.finish()方法所以不需要关闭流
         */
        // 这种写入Excel的思路是先调用EasyExcel的静态write(String pathName, Class head)方法指定文件路径和标题信息(返回ExcelWriterBuilder)
        // 然后调用sheet(String sheetName)方法指定sheet页的标号和名字等信息(返回ExcelWriterSheetBuilder)
        // 最后调用doWrite(List data)方法写入数据并关闭流。
        EasyExcel.write(fileName, DemoDataWith20.class).sheet("用户信息").doWrite(datas);
        LocalTime endTime = LocalTime.now();
        Duration between = Duration.between(startTime, endTime);
        LOGGER.info("写入Excel耗时：{}秒", between.getSeconds());
    }

    private List<DemoData> getDemoDatasWith5Field(int count) {
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

    private List<DemoDataWith20> getDemoDatasWith20Field(int count) {
        List<DemoDataWith20> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DemoDataWith20 demoData = new DemoDataWith20();
            datas.add(demoData);
            String birth = "1990-01-" + ((i % 30) < 9 ? ("0" + (i % 30 + 1)) : (i % 30 + 1));
            demoData.setName("curtis" + (i + 1));
            demoData.setAge(20 + (i % 10 + 1));
            demoData.setHeight(BigDecimal.valueOf(180 + (i % 10 + 1)));
            demoData.setBirth(birth);
            demoData.setSex(i % 2 == 0 ? "男" : "女");

            demoData.setName2("curtis" + (i + 1));
            demoData.setAge2(20 + (i % 10 + 1));
            demoData.setHeight2(BigDecimal.valueOf(180 + (i % 10 + 1)));
            demoData.setBirth2(birth);
            demoData.setSex2(i % 2 == 0 ? "男" : "女");

            demoData.setName3("curtis" + (i + 1));
            demoData.setAge3(20 + (i % 10 + 1));
            demoData.setHeight3(BigDecimal.valueOf(180 + (i % 10 + 1)));
            demoData.setBirth3(birth);
            demoData.setSex3(i % 2 == 0 ? "男" : "女");

            demoData.setName4("curtis" + (i + 1));
            demoData.setAge4(20 + (i % 10 + 1));
            demoData.setHeight4(BigDecimal.valueOf(180 + (i % 10 + 1)));
            demoData.setBirth4(birth);
            demoData.setSex4(i % 2 == 0 ? "男" : "女");

            demoData.setName5("curtis" + (i + 1));
            demoData.setAge5(20 + (i % 10 + 1));
            demoData.setHeight5(BigDecimal.valueOf(180 + (i % 10 + 1)));
            demoData.setBirth5(birth);
            demoData.setSex(i % 2 == 0 ? "男" : "女");
        }
        return datas;
    }
}
