package es.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author z
 */

@Slf4j
public class EsRestClientFactory {

    private static HttpHost[] getHttpHosts(String clientIps, int esHttpPort) {
        String[] clientIpList = clientIps.split(",");
        HttpHost[] httpHosts = new HttpHost[clientIpList.length];
        for (int i = 0; i < clientIpList.length; i++) {
            httpHosts[i] = new HttpHost(clientIpList[i], esHttpPort, "http");
        }
        return httpHosts;
    }

    /**
     * 创建不带HTTP Basic Auth认证rest客户端
     * @param clientIps 客户端列表信息
     * @param esHttpPort 客户端访问HTTP访问端口
     */
    public static RestHighLevelClient getRestHighLevelClient(String clientIps, int esHttpPort) {
        return new RestHighLevelClient(RestClient.builder(getHttpHosts(clientIps, esHttpPort)));
    }

    /**
     * 创建带HTTP Basic Auth认证rest客户端
     */
    public static RestHighLevelClient getRestHighLevelClient(String clientIps, int esHttpPort, String username, String password) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        return new RestHighLevelClient(RestClient.builder(getHttpHosts(clientIps, esHttpPort)).setHttpClientConfigCallback((HttpAsyncClientBuilder httpAsyncClientBuilder) -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)));
    }
}
