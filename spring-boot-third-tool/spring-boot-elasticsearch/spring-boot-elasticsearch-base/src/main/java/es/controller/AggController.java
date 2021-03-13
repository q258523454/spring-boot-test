package es.controller;

import es.client.EsRestClientFactory;
import es.service.ImportDataService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 常用聚合操作整理
 *
 * @author z
 */

@Slf4j
@RestController
public class AggController {
    @Value("${elasticsearch.clientIps}")
    private String clientIps;

    @Value("${elasticsearch.httpPort}")
    private int httpPort;

    @Value("${elasticsearch.username}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    @Value("${elasticsearch.indexName}")
    private String indexName;

    @Autowired
    private ImportDataService importDataService;

    @GetMapping("/insert")
    public void insert() {
        importDataService.importGenerateData();
    }


    /**
     * 平均id
     *
     * @return 平均id
     */
    @GetMapping("/avgId")
    public String importData() {
        try (RestHighLevelClient client = EsRestClientFactory.getRestHighLevelClient(clientIps, httpPort, username, password)) {
            double avgId = 0;
            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avgId").field("id");
            searchSourceBuilder.size(0).aggregation(avgAggregationBuilder);
            SearchResponse res = client.search(searchRequest.source(searchSourceBuilder), RequestOptions.DEFAULT);
            Avg avg = res.getAggregations().get("avgId");
            avgId = avg.getValue();
            return String.valueOf(avgId);
        } catch (Exception e) {
            log.error("均值聚合异常：{0}", e);
            return "查询失败";
        }
    }

}
