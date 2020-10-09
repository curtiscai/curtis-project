package com.curtis.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.curtis.easyexcel.model.DemoData;
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
 * @desc 使用EasyExcel多次写入数据测试类（更贴近实际情况，分页读取并写入，单次读取大量数据再写入可能会导致堆溢出的情况）
 * @date 2020-06-26
 * @email 397773935@qq.com
 * @reference
 */
public class RepeatWriteTest {

    private Logger LOGGER = LoggerFactory.getLogger(RepeatWriteTest.class);

    /**
     * 使用EasyExcel多次写入数据，多次调用ExcelWriter对象的write(List data, WriteSheet writeSheet)方法即可
     * EasyExcel会自动将数据从sheet页中数据末尾来进行填充写入。这样分页写入的方式对速度并没有什么影响。
     * 23:29:16.811 [main] INFO com.curtis.easyexcel.write.RepeatWriteTest - 写入Excel耗时：7秒
     * 23:29:42.939 [main] INFO com.curtis.easyexcel.write.RepeatWriteTest - 写入Excel耗时：8秒
     * 23:30:10.999 [main] INFO com.curtis.easyexcel.write.RepeatWriteTest - 写入Excel耗时：7秒
     */
    @Test
    public void testRepeatWrite() {
        // 文件路径为curtis-project/curtis-easyexcel/target/test-classes/用户-HHmm.xlsx
        String path = SimpleWriteTest.class.getResource("/").getPath();
        String hourMinuteStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String fileName = path + "用户-" + hourMinuteStr + ".xlsx";
        LOGGER.info("excel文件路径为：{}", fileName);

        LocalTime startTime = LocalTime.now();
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
            for (int i = 0; i < 100; i++) {
                List<DemoData> datas = this.getDemoDatas(i * 100, 100);
                excelWriter.write(datas, writeSheet);
                datas.clear(); // notice gc
            }
        } catch (Exception e) {

        } finally {
            if (excelWriter != null)
                excelWriter.finish(); // 需要手动关闭流
        }
//        EasyExcel.write(fileName, DemoData.class).sheet("用户信息").doWrite(datas);
        LocalTime endTime = LocalTime.now();
        Duration between = Duration.between(startTime, endTime);
        LOGGER.info("写入Excel耗时：{}秒", between.getSeconds());
    }

    /**
     * 使用EasyExcel多次写入数据，每次写入新的sheet，只需要每次迭代时创建新的WriteSheet，
     * 然后在调用ExcelWriter对象的write(List data, WriteSheet writeSheet)方法传入不同只需要每次迭代时创建新的WriteSheet对象即可
     * EasyExcel会自动将数据从sheet页中数据末尾来进行填充写入。这样分页写入的方式对速度并没有什么影响。
     * 23:29:16.811 [main] INFO com.curtis.easyexcel.write.RepeatWriteTest - 写入Excel耗时：7秒
     * 23:29:42.939 [main] INFO com.curtis.easyexcel.write.RepeatWriteTest - 写入Excel耗时：8秒
     * 23:30:10.999 [main] INFO com.curtis.easyexcel.write.RepeatWriteTest - 写入Excel耗时：7秒
     */
    @Test
    public void testRepeatWriteWithMultiSheet() {
        // 文件路径为curtis-project/curtis-easyexcel/target/test-classes/用户-HHmmss.xlsx
        String path = SimpleWriteTest.class.getResource("/").getPath();
        String hourMinuteStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String fileName = path + "用户-" + hourMinuteStr + ".xlsx";
        LOGGER.info("excel文件路径为：{}", fileName);

        LocalTime startTime = LocalTime.now();
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
            for (int i = 0; i < 100; i++) {
                // 数据写入不同sheet则需要每次迭代时创建新sheet页即可
                WriteSheet writeSheet = EasyExcel.writerSheet("用户信息" + (i + 1)).build();
                List<DemoData> datas = this.getDemoDatas(i * 100, 100);
                excelWriter.write(datas, writeSheet);
                datas.clear(); // notice gc
            }
        } catch (Exception e) {

        } finally {
            if (excelWriter != null)
                excelWriter.finish(); // 需要手动关闭流
        }
//        EasyExcel.write(fileName, DemoData.class).sheet("用户信息").doWrite(datas);
        LocalTime endTime = LocalTime.now();
        Duration between = Duration.between(startTime, endTime);
        LOGGER.info("写入Excel耗时：{}秒", between.getSeconds());
    }

    private List<DemoData> getDemoDatas(int start, int count) {
        List<DemoData> datas = new ArrayList<>();
        for (int i = start; i < start + count; i++) {
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
}
