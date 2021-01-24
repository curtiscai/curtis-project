package com.curtis.elasticsearch.raw;

import com.curtis.elasticsearch.model.Teacher;
import com.curtis.elasticsearch.utils.ESClientUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author curtis
 * @desc
 * @date 2021-01-23
 * @email 397773935@qq.com
 * @reference https://www.elastic.co/guide/en/elasticsearch/reference/6.8/indices.html
 */
public class DocumentTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTest.class);

    @Test
    public void testCreateDoc() throws IOException, ParseException {
        String indexName = "teacher";
        Teacher teacher = new Teacher(1L, "curtis1", new SimpleDateFormat("yyyy-MM-dd").parse("1991-01-01"), 15010010001L, BigDecimal.valueOf(181.11));
        String teacherJsonStr = new ObjectMapper().writeValueAsString(teacher);


        IndexRequest indexRequest = new IndexRequest(indexName, "_doc", String.valueOf(teacher.getId()));
        indexRequest.source(teacherJsonStr, XContentType.JSON);

        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();

        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        LOGGER.info("The change that occurred to the document：{}", indexResponse.getResult());
    }

    @Test
    public void testUpdateDoc1() throws IOException, ParseException {
        String indexName = "teacher";
        Teacher teacher = new Teacher(1L, "curtis1", new SimpleDateFormat("yyyy-MM-dd").parse("1991-01-01"), 15010010001L, BigDecimal.valueOf(181.11));
        String teacherJsonStr = new ObjectMapper().writeValueAsString(teacher);


        UpdateRequest updateRequest = new UpdateRequest(indexName, "_doc", String.valueOf(teacher.getId()));
        updateRequest.doc(teacherJsonStr, XContentType.JSON);

        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();

        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        LOGGER.info("The change that occurred to the document：{}", updateResponse.getResult());
    }

    @Test
    public void testUpdateDoc2() throws IOException {
        String indexName = "teacher";

        Long id = 1L;
        Map<String,Object> docMap = Maps.newHashMap();
        docMap.put("name","curtis11");
        docMap.put("phone",15010010011L);

        UpdateRequest indexRequest = new UpdateRequest(indexName, "_doc", String.valueOf(id));
        indexRequest.doc(docMap);

        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();

        UpdateResponse updateResponse = restHighLevelClient.update(indexRequest, RequestOptions.DEFAULT);
        LOGGER.info("The change that occurred to the document：{}", updateResponse.getResult());
    }

    @Test
    public void testDeleteDoc() throws IOException {
        String indexName = "teacher";

        Long id = 1L;
        DeleteRequest deleteRequest = new DeleteRequest(indexName, "_doc", String.valueOf(id));

        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();

        DeleteResponse updateResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        LOGGER.info("The change that occurred to the document：{}", updateResponse.getResult());
    }
}
