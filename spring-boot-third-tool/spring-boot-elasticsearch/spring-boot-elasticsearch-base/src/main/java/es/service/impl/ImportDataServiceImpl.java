package es.service.impl;

import es.client.EsRestClientFactory;
import es.service.ImportDataService;
import es.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * @author zz
 * 由于数据导入是一个耗时操作，同时可以根据配置中具体的参数进行对应的导入处理，因此单独写一个启动任务来处理
 */

@Component
@Slf4j
public class ImportDataServiceImpl implements ImportDataService {
    @Value("${elasticsearch.clientIps}")
    private String clientIps;

    @Value("${elasticsearch.httpPort}")
    private int httpPort;

    @Value("${elasticsearch.username}")
    private String username;

    @Value("${elasticsearch.password}")
    private String password;

    @Value("${elasticsearch.docNum}")
    private int docNum;

    @Value("${elasticsearch.indexType}")
    private String indexType;

    @Value("${elasticsearch.indexName}")
    private String indexName;

    @Value("${elasticsearch.bulkSize}")
    private int bulkSize;

    @Value("${kafka.servers:''}")
    private String kafkaServers;

    @Value("${kafka.username:''}")
    private String kafkaUsername;

    @Value("${kafka.password:''}")
    private String kafkaPassword;

    /**
     * 通过自动生成数据的方式导入ES
     */
    @Override
    public void importGenerateData() {
        try (RestHighLevelClient client = EsRestClientFactory.getRestHighLevelClient(clientIps, httpPort, username, password)) {
            log.info("开始导入数据:{}", new Date());
            // 用于填充name字段的数据
            String[] names = {"aa", "bb", "cc", "dd"};
            // 如果docNum无法整除bulkSize，那么数据可能遗漏几条
            for (int j = bulkSize; j <= docNum; j += bulkSize) {
                BulkRequest bulkRequest = new BulkRequest();
                for (int k = 0; k < bulkSize; k++) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", new Random().nextInt(120));
                    data.put("name", names[new Random().nextInt(4)]);
                    data.put("time", DateUtil.getY_m_d_H_m_s());
                    bulkRequest.add(new IndexRequest(indexName, indexType).source(data));
                }
                try {
                    BulkResponse bulkResponse = client.bulk(bulkRequest);
                    if (bulkResponse.hasFailures()) {
                        // TODO 这里还有优化的空间，如果只有部分数据写入失败，需要根据失败信息精细处理，或者粗糙一点这次bulk全部重试
                        log.error("bulk写入异常返回值:{}", bulkResponse.buildFailureMessage());
                    }
                } catch (ConnectException e) {
                    log.error("bulk连接异常:{0}", e);
                    j -= bulkSize;
                } catch (IOException e) {
                    log.error("bulk写入异常:{0}", e);
                    j -= bulkSize;
                }
                log.info("已导入{}个文档", j);
            }
            log.info("结束导入数据:" + new Date());
        } catch (Exception e) {
            log.error("写入异常{0}", e);
        }
    }
}
