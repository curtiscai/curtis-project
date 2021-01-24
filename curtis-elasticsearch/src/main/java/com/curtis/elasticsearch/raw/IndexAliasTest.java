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
import org.elasticsearch.common.xcontent.XContentType;
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
public class IndexAliasTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexAliasTest.class);


    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.8/java-rest-high-create-index.html
     *
     * @throws IOException
     */
    @Test
    public void testCreateIndex() throws IOException {
        String indexName = "teacher-102";
        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();

        // Create Index Requestedit
        // A CreateIndexRequest requires an index argument:
        CreateIndexRequest request = new CreateIndexRequest(indexName);

        // Index settingsedit
        // Each index created can have specific settings associated with it.
        request.settings(Settings.builder()
                .put(IndexMetaData.SETTING_NUMBER_OF_SHARDS, 3)
                .put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, 2));


        // Index mappingsedit
        // An index may be created with mappings for its document types
        request.mapping(
                "{\n" +
                        "   \"properties\": {\n" +
                        "      \"name\": {\n" +
                        "         \"type\": \"text\"\n" +
                        "      },\n" +
                        "      \"birth\": {\n" +
                        "         \"type\": \"date\",\n" +
                        "         \"format\": \"yyyy-MM-dd\"\n" +
                        "      },\n" +
                        "      \"phone\": {\n" +
                        "         \"type\": \"long\"\n" +
                        "      },\n" +
                        "      \"height\": {\n" +
                        "         \"type\": \"double\"\n" +
                        "      }\n" +
                        "   }\n" +
                        "}",
                XContentType.JSON);


        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        // 4. 通过client连接ES并执行索引创建
        LOGGER.info("whether the response is acknowledged or not：{}", createIndexResponse.isAcknowledged());
    }

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.8/java-rest-high-update-aliases.html
     */
    @Test
    public void test() throws IOException {
        String indexName = "teacher-102";
        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();
        // Indices Aliases Requestedit
        // The Index Aliases API allows aliasing an index with a name, with all APIs automatically converting the alias name to the actual index name.
        // An IndicesAliasesRequest must have at least one AliasActions:
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasAction =
                new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                        .index(indexName)
                        .alias("teacher-alias-2");
        request.addAliasAction(aliasAction);

        // Synchronous Executionedit
        // When executing a IndicesAliasesRequest in the following manner, the client waits for the IndicesAliasesResponse to be returned before continuing with code execution:
        AcknowledgedResponse indicesAliasesResponse =
                restHighLevelClient.indices().updateAliases(request, RequestOptions.DEFAULT);
        LOGGER.info("whether the response is acknowledged or not：{}", indicesAliasesResponse.isAcknowledged());

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
