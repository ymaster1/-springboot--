package com.ym.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ym.provider.commons.utils.ApplicationContextHolder;
import com.ym.provider.entity.UserEsDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SpringBootTest
@Slf4j
class ProviderApplicationTests {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建一个空索引
     * 存在会抛异常
     * @throws IOException
     */
    @Test
    void esAddIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("ymaster2");
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        boolean acknowledged = response.isAcknowledged();
        System.out.println(acknowledged);
        log.info("{}", JSONObject.toJSON(acknowledged));
    }

    /**
     * 索引是否存在
     *
     * @throws IOException
     */
    @Test
    void esExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("ymaster1");
        boolean response = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        log.info("{}", JSONObject.toJSON(response));
    }

    /**
     * 删除索引
     *
     * @throws IOException
     */
    @Test
    void esDelIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("ymaster1");
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        log.info("{}", JSONObject.toJSON(response));
    }

    /**
     * 新增一条记录
     *
     * @throws IOException
     */
    @Test
    void esAddDoc() throws IOException {
        UserEsDto dto = new UserEsDto();
        dto.setAdder("某个屯儿");
        dto.setCity("达州");
        dto.setConcatPhone("911");
        dto.setCountry("CN");
        dto.setSex(1);
        dto.setUserName("那个谁");

        IndexRequest request = new IndexRequest("ymaster2");
//      如果没有特别要求可以不用传，es会自动生成无序唯一ID
//        request.id("");
//        需要指定解析的类型
        request.source(JSONObject.toJSONString(dto), XContentType.JSON);
//        request.source(JSON.toJSON(dto), XContentType.JSON); 用toJSON会把整个json当成key,错误的
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        RestStatus status = response.status();
        log.info("{}", JSONObject.toJSON(status));
    }
    /**
     * 新增一条记录
     *
     * @throws IOException
     */
    @Test
    void esAddDocByMap() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("adder","那条街");
        map.put("city","达州");
        map.put("concatPhone","999");
        map.put("country","CN");
        map.put("sex",1);
        map.put("usserName","那个谁");
        IndexRequest request = new IndexRequest("ymaster2");
//      如果没有特别要求可以不用传，es会自动生成无序唯一ID
//        request.id("");
//        需要指定解析的类型
        request.source(JSONObject.toJSONString(map), XContentType.JSON);
//        request.source(JSON.toJSON(dto), XContentType.JSON); 用toJSON会把整个json当成key,错误的
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        RestStatus status = response.status();
        log.info("{}", JSONObject.toJSON(status));
    }
    /**
     * 获取一条记录
     *
     * @throws IOException
     */
    @Test
    void esGetDoc() throws IOException {
        GetRequest request = new GetRequest("ymaster1","k5xrm3UBqwrudmm9EVyV");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        Map<String, Object> sourceAsMap = response.getSourceAsMap();
        String sourceAsString = response.getSourceAsString();

        log.info("{}", JSONObject.toJSON(sourceAsMap));
        log.info("{}", JSONObject.toJSON(sourceAsString));
    }

    /**
     * 更新一条记录，需要指定id
     * @throws IOException
     */
    @Test
    void esUpdateDoc() throws IOException {
        UserEsDto dto = new UserEsDto();
        dto.setAdder("那条街");
        dto.setCity("达州");
        dto.setConcatPhone("911");
        dto.setCountry("CN");
        dto.setSex(1);
        dto.setUserName("黑娃儿");
        UpdateRequest request = new UpdateRequest("ymaster1","fJzCl3UBqwrudmm9xly1");
//        需要指定解析的类型
        request.doc(JSONObject.toJSONString(dto), XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        log.info("{}", JSONObject.toJSON(response));
    }

    /**
     * 删除一条文档
     * @throws IOException
     */
    @Test
    void esDelDoc() throws IOException {
        DeleteRequest request = new DeleteRequest("ymaster1","Urk0sXUBUPAjrA7GW0jD");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        RestStatus status = response.status();
        log.info("{}", JSONObject.toJSON(status));
    }

    /**
     * 批量操作
     * @throws IOException
     */
    @Test
    void esBatchDoc() throws IOException {
//        创建批处理请求
        BulkRequest request = new BulkRequest();
        List<UserEsDto> list = new ArrayList<>();
        UserEsDto dto1 = new UserEsDto();
        UserEsDto dto2 = new UserEsDto();
        UserEsDto dto3 = new UserEsDto();
        UserEsDto dto4 = new UserEsDto();
        dto1.setUserName("张三");
        dto2.setUserName("李四");
        dto3.setUserName("王五");
        dto4.setUserName("刘能");
        list.add(dto1);
        list.add(dto2);
        list.add(dto3);
        list.add(dto4);
//        根据实际操作更换相应请求即可
        list.forEach(e-> request.add(new IndexRequest("ymaster1")
        .source(JSONObject.toJSONString(e),XContentType.JSON)));
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        log.info("{}", JSONObject.toJSON(response));
    }
    /**
     * 批量查询文档
     * @throws IOException
     */
    @Test
    void esSearchDoc() throws IOException {
//        指定index搜索,可以指定多个
        SearchRequest request = new SearchRequest("ymaster1");
//        构建搜索条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        查询条件可以使用QueryBuilders快速构建，这里matchAllQuery查询所有
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        request.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Arrays.stream(response.getHits().getHits()).forEach(e->{
            log.info("{}", JSONObject.toJSON(e));
        });
    }
    @Test
    public void testTask() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object bean = ApplicationContextHolder.getBean("timingTaskService");
        Method testTask = bean.getClass().getDeclaredMethod("testTask", null);
        testTask.invoke(bean);
        System.out.println(bean.getClass().getName());
    }

}
