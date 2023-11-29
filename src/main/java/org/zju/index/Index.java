package org.zju.index;

import com.alibaba.fastjson2.JSON;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.zju.entity.Paper;

import java.io.IOException;

/**
 * 操作索引
 * @author Tao
 * @date 2023/11/29
 */
public class Index {
    int count = 100;

    /**
     * 用于认证授权ES
     */
    private final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    /**
     * 操作ES的工具类
     */
    private final RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    HttpHost.create("http://124.71.196.104:9200")
            ).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                    credentialsProvider.setCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials("elastic", "elastic"));
                    httpAsyncClientBuilder.disableAuthCaching();
                    return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }
            }));

    /**
     * //测试索引库是否存在
     * @throws IOException
     */
    public void testGetIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest("paper");
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * //根据id获取索引文档
     * @throws IOException
     */
    public Paper searchById(Integer id) throws IOException {
        SearchRequest request = new SearchRequest("paper");
        request.source().query(QueryBuilders.matchQuery("id", id.toString()));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String json = searchHit.getSourceAsString();
            return JSON.parseObject(json, Paper.class);
        }
        return null;
    }

    /**
     * 向索引库插入文档
     * @param paper
     * @throws IOException
     */
    public void insertDocument(Paper paper) throws IOException {
        IndexRequest request = new IndexRequest("paper").id(paper.getId().toString());
        request.source(JSON.toJSONString(paper), XContentType.JSON);
        IndexResponse index = client.index(request, RequestOptions.DEFAULT);
    }


    /**
     * 查找索引
     * @param s
     * @throws IOException
     */
    public void search(String s) throws IOException {
        SearchRequest request = new SearchRequest("paper");
        request.source().query(QueryBuilders.matchPhraseQuery("all", s));
        handleResult(request);
    }

    /**
     * 查找所有文档
     * @throws IOException
     */
    public void searchAll() throws IOException {
        SearchRequest request = new SearchRequest("paper");
        request.source().query(QueryBuilders.matchAllQuery()).size(count);
        handleResult(request);
    }

    /**
     * 释放资源
     * @throws IOException
     */
    public void releaseResource() throws IOException {
        this.client.close();
    }

    /**
     * //解析索引库查询结果
     * @param request
     * @throws IOException
     */
    private void handleResult(SearchRequest request) throws IOException {
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String json = searchHit.getSourceAsString();
            Paper paper = JSON.parseObject(json, Paper.class);
            System.out.println(paper.toString());
        }
    }

}
