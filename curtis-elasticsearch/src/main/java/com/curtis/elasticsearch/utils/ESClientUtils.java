package com.curtis.elasticsearch.utils;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ESClientUtils {

    public static RestHighLevelClient getClient(){
        // 创建HttpHost
        HttpHost httpHost = new HttpHost("192.168.2.101",9200);
        // 创建RestClientBuilder
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        // 创建RestHighLevelClient
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return restHighLevelClient;
    }
}
