package com.curtis.elasticsearch.utils;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

public class ESClientUtilsTest {

    @Test
    public void testESConnect(){
        RestHighLevelClient restHighLevelClient = ESClientUtils.getClient();
        System.out.println(restHighLevelClient);
    }
}
