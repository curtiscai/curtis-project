package com.curtis.elasticsearch.raw;

import com.curtis.elasticsearch.utils.ESClientUtils;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author curtis
 * @desc
 * @date 2021-01-23
 * @email 397773935@qq.com
 * @reference https://www.elastic.co/guide/en/elasticsearch/reference/6.8/indices.html
 */
public class IndexTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexTest.class);

    static class Fields {
        static final String ALIASES = "aliases";
        static final String MAPPINGS = "mappings";
        static final String SETTINGS = "settings";
        static final String WARMERS = "warmers";
    }

    @Test
    public void testCreateIndex() throws IOException {
        String indexName = "teacher";

        RestHighLevelClient client = ESClientUtils.getClient();
        // 1. 设置关于索引的settings
        Settings.Builder settings = Settings.builder()
                .put(IndexMetaData.SETTING_NUMBER_OF_SHARDS, 3)
                .put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, 1);

        // 2. 创建索引的结构mappings
        XContentBuilder mappings = JsonXContent.contentBuilder()
                .startObject()
                    .startObject("properties")
                        .startObject("name")
                            .field("type","text")
                        .endObject()
                        .startObject("birth")
                            .field("type","date")
                            .field("format","yyyy-MM-dd")
                        .endObject()
                        .startObject("phone")
                            .field("type","long")
                        .endObject()
                        .startObject("height")
                            .field("type","double")
                        .endObject()
                    .endObject()
                .endObject();

        // 3. 将settings和mappings封装到Request
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName)
                .settings(settings)
                .mapping(mappings);

        // 4. 通过client连接ES并执行索引创建
        CreateIndexResponse createIndexResponse = client.indices().create(indexRequest, RequestOptions.DEFAULT);
        LOGGER.info("whether the response is acknowledged or not：{}",createIndexResponse.isAcknowledged());
    }

    @Test
    public void testExists() throws IOException {
        String indexName = "teacher";
        RestHighLevelClient client = ESClientUtils.getClient();

        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        LOGGER.info("Checks if the index (indices) exists or not.：{}", exists);

        GetIndexRequest getIndexRequest2 = new GetIndexRequest("teacher_2");
        boolean exists2 = client.indices().exists(getIndexRequest2, RequestOptions.DEFAULT);
        LOGGER.info("Checks if the index (indices) exists or not.：{}", exists2);
    }



    @Test
    public void testDelete() throws IOException {
        String indexName = "teacher_1";
        RestHighLevelClient client = ESClientUtils.getClient();

        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        LOGGER.info("Deletes an index using the Delete Index API.：{}", delete);
    }

    @Test
    public void testIndex() throws IOException {
        String indexName = "teacher";
        RestHighLevelClient client = ESClientUtils.getClient();

        IndicesAliasesRequest request = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasAction =
                new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                        .index(indexName)
                        .alias("alias1")
                        .alias("alias2");
        request.addAliasAction(aliasAction);
        AcknowledgedResponse acknowledgedResponse = client.indices().updateAliases(request, RequestOptions.DEFAULT);
        LOGGER.info("Updates aliases using the Index Aliases API.：{}", acknowledgedResponse);

    }
}
