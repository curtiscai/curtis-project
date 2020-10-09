package com.curtis.easyexcel.read.simple;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.curtis.easyexcel.model.DemoData;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author curtis
 * @desc
 * @date 2020-06-29
 * @email 397773935@qq.com
 * @reference
 */
public class ReadWithExceptionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadWithExceptionTest.class);

    @Test
    public void testWriteWithException() {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        String fileName = Thread.currentThread().getContextClassLoader().getResource("file/user-5c-20-ex.xlsx").getPath();

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
            excelReader = EasyExcel.read(fileName, DemoData.class, new ReadDataWithExceptionHandlerListener()).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } catch (Exception e) {
            // 只要是在读监听器抛出的异常都是ExcelAnalysisException，具体的异常在ExcelAnalysisException对象的cause属性中
            if (e instanceof ExcelAnalysisException) {
                Throwable cause = e.getCause();
                if (cause instanceof NullPointerException) {
                    NullPointerException nullPointerException = (NullPointerException) cause;
                    LOGGER.info("捕获自定义的空指针异常，异常信息为：" + nullPointerException.getClass().getName() + ": " + nullPointerException.getMessage());
                } else if (cause instanceof RuntimeException) {
                    RuntimeException runtimeException = (RuntimeException) cause;
                    LOGGER.info("捕获自定义的运行时异常，异常信息为：" + runtimeException.getClass().getName() + ": " + runtimeException.getMessage());
                } else {
                    LOGGER.info("捕获其他异常，异常信息为：" + cause.getClass().getName() + ": " + cause.getMessage());
                }
            }
            // 非读监听器中抛出的异常
            else {
                LOGGER.info("捕获到非读监听器异常，异常信息为：" + e.getClass().getName() + ": " + e.getMessage());
            }
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }

    // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
    public class ReadDataWithExceptionHandlerListener extends AnalysisEventListener<DemoData> {
        private final Logger LOGGER = LoggerFactory.getLogger(SimpleReadTest.DemoDataListener.class);
        /**
         * 可以根据数量调整批量处理的数据个数，每次操作完清理list，方便内存回收
         */
        private static final int BATCH_COUNT = 15;

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
                this.saveData();
                // 存储完成清理 list
                dataList.clear();
                // 模拟数据处理过程中发生了异常
                if (true) {
                    throw new NullPointerException("自定义的空指针异常");
                }
            }
        }

        /**
         * 所有数据解析完成了 都会来调用
         *
         * @param context
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 这里也要保存数据，确保最后遗留的数据也存储到数据库
            this.saveData();
            dataList.clear();

            LOGGER.info("所有数据解析完成！");
        }

        /**
         * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
         *
         * @param exception
         * @param context
         * @throws Exception
         */
        @Override
        public void onException(Exception exception, AnalysisContext context) throws Exception {
            LOGGER.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
            // 如果是某一个单元格的转换异常 能获取到具体行号
            // 如果要获取头的信息 配合invokeHeadMap使用
            if (exception instanceof ExcelDataConvertException) {
                /**
                 * ExcelDataConvertException持有的三个属性：
                 * rowIndex：错误发生的行数
                 * columnIndex：错误发生的列数
                 * cellData：错误的数据，注意不要调用cellData的getStringValue或者setNumberValue等等与具体类型相关的数据返回值，
                 * 要获取cellData的数据时直接调用toString()方法即可，toString()内部实现会根据类型来获取对应属性的值。
                 */
                ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
                // 注意这里的行号和列号都是从0开始的
                LOGGER.error("第{}行，第{}列解析异常，错误数据是：{}，具体的错误异常是：{}", excelDataConvertException.getRowIndex() + 1,
                        excelDataConvertException.getColumnIndex() + 1, excelDataConvertException.getCellData().toString(),
                        excelDataConvertException.getCause() == null ? null :
                                excelDataConvertException.getCause().getClass().getName() + ": " + excelDataConvertException.getCause().getMessage());
            } else {
                // 如果不处理异常而是直接扔出去则解析会中断
                throw exception;
            }
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
