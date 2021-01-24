package com.curtis.elasticsearch.raw;

import com.curtis.elasticsearch.model.Teacher;
import com.curtis.elasticsearch.utils.ESClientUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
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

/**
 * @author curtis
 * @desc
 * @date 2021-01-23
 * @email 397773935@qq.com
 * @reference https://www.elastic.co/guide/en/elasticsearch/reference/6.8/indices.html
 */
public class DocumentBulkTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentBulkTest.class);

    @Test
    public void testCreateDoc() throws IOException, ParseException {
        String indexName = "teacher";
        Teacher teacher1 = new Teacher(1L, "curtis1", new SimpleDateFormat("yyyy-MM-dd").parse("1991-01-11"), 15010010001L, BigDecimal.valueOf(181.11));
        Teacher teacher2 = new Teacher(2L, "curtis2", new SimpleDateFormat("yyyy-MM-dd").parse("1992-02-12"), 15010010002L, BigDecimal.valueOf(182.22));
        Teacher teacher3 = new Teacher(3L, "curtis3", new SimpleDateFormat("yyyy-MM-dd").parse("1993-03-13"), 15010010003L, BigDecimal.valueOf(183.33));
        String teacherJsonStr1 = new ObjectMapper().writeValueAsString(teacher1);
        String teacherJsonStr2 = new ObjectMapper().writeValueAsString(teacher2);
        String teacherJsonStr3 = new ObjectMapper().writeValueAsString(teacher3);


        BulkRequest bulkRequest = new BulkRequest(indexName,"_doc");
        bulkRequest.add(new IndexRequest(indexName, "_doc", String.valueOf(teacher1.getId())).source(teacherJsonStr1, XContentType.JSON));
        bulkRequest.add(new IndexRequest(indexName, "_doc", String.valueOf(teacher2.getId())).source(teacherJsonStr2, XContentType.JSON));
        bulkRequest.add(new IndexRequest(indexName, "_doc", String.valueOf(teacher3.getId())).source(teacherJsonStr3, XContentType.JSON));

        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
        LOGGER.info("Has anything failed with the execution.：{}", bulkResponse.hasFailures());
        LOGGER.info("status：{}", bulkResponse.status());
    }

    @Test
    public void testDeleteDoc() throws IOException, ParseException {
        String indexName = "teacher";
        Long id1 = 1L;
        Long id2 = 2L;
        Long id3 = 3L;

        BulkRequest bulkRequest = new BulkRequest(indexName,"_doc");
        bulkRequest.add(new DeleteRequest(indexName, "_doc", String.valueOf(id1)));
        bulkRequest.add(new DeleteRequest(indexName, "_doc", String.valueOf(id2)));
        bulkRequest.add(new DeleteRequest(indexName, "_doc", String.valueOf(id3)));

        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();

        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
        LOGGER.info("Has anything failed with the execution.：{}", bulkResponse.hasFailures());
        LOGGER.info("status：{}", bulkResponse.status());
    }
}
