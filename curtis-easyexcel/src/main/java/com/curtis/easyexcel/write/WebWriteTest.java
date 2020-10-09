package com.curtis.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.curtis.easyexcel.model.DemoData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author curtis
 * @desc 使用EasyExcel写入数据并写入Http输出流测试类
 * @date 2020-06-27
 * @email 397773935@qq.com
 * @reference
 */
@RequestMapping("/api")
@Controller
public class WebWriteTest {

    /**
     * Test URL:http://localhost:8080/api/download
     *
     * @param response
     * @throws Exception
     */
    @GetMapping("/download")
    public void testWriteToWeb(HttpServletResponse response) throws Exception {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), DemoData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(this.getDemoDatas(1_0000));
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = Maps.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(new ObjectMapper().writeValueAsString(map));
        }
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
}
