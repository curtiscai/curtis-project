package com.curtis.easyexcel.read.simple;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.curtis.easyexcel.model.DemoData;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author curtis
 * @desc 使用EasyExcel读取数据测试类
 * @date 2020-06-29
 * @email 397773935@qq.com
 * @reference
 */
public class SimpleReadTest {

    /**
     * 读取Excel数据测试-使用Model：最简单的写入Excel方式一-自动关闭流
     */
    @Test
    public void testSimpleRead1() {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        String fileName = Thread.currentThread().getContextClassLoader().getResource("file/user-5c-100.xlsx").getPath();
        /**
         * public static ExcelReaderBuilder read(String pathName, Class head, ReadListener readListener)
         * pathName用于指定文件名称，重写的方法里面可以传入InputStream，InputStream更常用，因为通常都是接收网络上传的Excel文件
         * head用于指定解析Excel数据使用的类（可以根据列标号绑定数据，也可以使用列名绑定数据，推荐后者，但是需要注意Excel里列名重复可能导致的问题）
         * readListener用于指定读取数据的监听器，数据读取和异常处理都在监听器中进行。
         *
         * public ExcelReaderSheetBuilder sheet(String sheetName)
         * public ExcelReaderSheetBuilder sheet(Integer sheetNo)
         * sheet方法中sheetName用于指定读取的sheet页的名字，sheetNo用于指定读取sheet的编号。
         *
         * public void doRead()方法用于执行读取Excel操作。
         */
        // 这里 需要指定读用哪个class去读，然后读取sheet名字为用户信息的sheet，读取完毕后文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet("用户信息").doRead();
    }

    /**
     * 读取Excel数据测试-使用Model：最简单的写入Excel方式二-自动关闭流
     */
    @Test
    public void testSimpleRead2() {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        String fileName = Thread.currentThread().getContextClassLoader().getResource("file/user-5c-100.xlsx").getPath();

        ExcelReader excelReader = null;
        try {
            /**
             * public static ExcelReaderBuilder read(String pathName, Class head, ReadListener readListener)
             * pathName用于指定文件名称，重写的方法里面可以传入InputStream，InputStream更常用，因为通常都是接收网络上传的Excel文件
             * head用于指定解析Excel数据使用的类（可以根据列标号绑定数据，也可以使用列名绑定数据，推荐后者，但是需要注意Excel里列名重复可能导致的问题）
             * readListener用于指定读取数据的监听器，数据读取和异常处理都在监听器中进行。
             *
             * public static ExcelReaderSheetBuilder readSheet(Integer sheetNo)
             * public static ExcelReaderSheetBuilder readSheet(String sheetName)
             * sheet方法中sheetName用于指定读取的sheet页的名字，sheetNo用于指定读取sheet的编号。
             *
             * public ExcelReader read(ReadSheet... readSheet)
             * public ExcelReader read(List<ReadSheet> readSheetList)
             * ExcelReader的实例read方法用于执行读取指定sheet的操作。支持读取多个sheet页
             */
            // 首先根据文件、解析类、读取监听器来创建ExcelReader对象，然后根据sheet页名称或者标号创建ReadSheet对象，
            // 最后调用ExcelReader对象的read(ReadSheet... readSheet)方法来读取指定sheet的数据。
            excelReader = EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream()
    }


    @Test
    public void testReadExcelInfo() {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        String fileName = Thread.currentThread().getContextClassLoader().getResource("file/user-5c-multi-sheet.xlsx").getPath();

        ExcelReader excelReader = null;
        try {
            /**
             * public static ExcelReaderBuilder read(String pathName, Class head, ReadListener readListener)
             * pathName用于指定文件名称，重写的方法里面可以传入InputStream，InputStream更常用，因为通常都是接收网络上传的Excel文件
             * head用于指定解析Excel数据使用的类（可以根据列标号绑定数据，也可以使用列名绑定数据，推荐后者，但是需要注意Excel里列名重复可能导致的问题）
             * readListener用于指定读取数据的监听器，数据读取和异常处理都在监听器中进行。
             *
             * public static ExcelReaderSheetBuilder readSheet(Integer sheetNo)
             * public static ExcelReaderSheetBuilder readSheet(String sheetName)
             * sheet方法中sheetName用于指定读取的sheet页的名字，sheetNo用于指定读取sheet的编号。
             *
             * public ExcelReader read(ReadSheet... readSheet)
             * public ExcelReader read(List<ReadSheet> readSheetList)
             * ExcelReader的实例read方法用于执行读取指定sheet的操作。支持读取多个sheet页
             */
            // 首先根据文件、解析类、读取监听器来创建ExcelReader对象，然后根据sheet页名称或者标号创建ReadSheet对象，
            // 最后调用ExcelReader对象的read(ReadSheet... readSheet)方法来读取指定sheet的数据。
            excelReader = EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).build();
            AnalysisContext analysisContext = excelReader.analysisContext();

            ReadRowHolder readRowHolder = analysisContext.readRowHolder();
            ReadSheetHolder readSheetHolder = analysisContext.readSheetHolder();
            // Approximate total number of rows 获取近似的总行数
//            Integer approximateTotalRowNumber = readSheetHolder.getApproximateTotalRowNumber();

            ExcelReadExecutor excelReadExecutor = excelReader.excelExecutor();
            List<ReadSheet> readSheets = excelReadExecutor.sheetList();
            for (ReadSheet readSheet : readSheets) {
                List<List<String>> head = readSheet.getHead();
                Integer headRowNumber = readSheet.getHeadRowNumber();
                Boolean autoTrim = readSheet.getAutoTrim();
                readSheet.setAutoTrim(true);
                readSheet.setHeadRowNumber(1);

            }

            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream()
    }


    // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
    public class DemoDataListener extends AnalysisEventListener<DemoData> {
        private final Logger LOGGER = LoggerFactory.getLogger(DemoDataListener.class);
        /**
         * 可以根据数量调整批量处理的数据个数，每次操作完清理list，方便内存回收
         */
        private static final int BATCH_COUNT = 80;

        /**
         * 用于暂存数据
         */
        List<DemoData> dataList = Lists.newArrayList();

        /**
         * 这个每一条数据解析都会来调用
         *
         * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
         * @param context
         */
        @Override
        public void invoke(DemoData data, AnalysisContext context) {
            LOGGER.info("解析到一条数据:{}", JSONUtil.toJsonStr(data));
            dataList.add(data);
            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (dataList.size() >= BATCH_COUNT) {
                saveData();
                // 存储完成清理 list
                dataList.clear();
            }

            // 上下文中可以得到的信息

        }

        /**
         * 所有数据解析完成了 都会来调用
         *
         * @param context
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
            saveData();
            LOGGER.info("所有数据解析完成！");
        }

        /**
         * 加上存储数据库
         */
        private void saveData() {
            LOGGER.info("{}条数据，开始存储数据库！", dataList.size());
            LOGGER.info("存储数据库成功！");
        }
    }
}
