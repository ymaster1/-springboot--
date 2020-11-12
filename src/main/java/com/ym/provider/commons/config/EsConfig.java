package com.ym.provider.commons.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ymaster1
 * @date 2020/11/5 16:38
 * @description
 */
@Configuration
public class EsConfig {
    /**
     *  通过nacos读取ip
     */
    @Value("${elasticsearch.host}")
    private String host;
    /**
     * 需要配置一个客户端，如果是集群可以配置多个host
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        String[] hosts = host.split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < httpHosts.length; i++) {
            String h = hosts[i];
            httpHosts[i] = new HttpHost(h.split(":")[0], Integer.parseInt(h.split(":")[1]));
        }
        RestClientBuilder builder = RestClient.builder(httpHosts);
        return new RestHighLevelClient(builder);
    }
}
