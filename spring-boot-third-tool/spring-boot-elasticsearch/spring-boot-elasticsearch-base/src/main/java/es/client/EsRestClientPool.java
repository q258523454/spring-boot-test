package es.client;

import org.elasticsearch.client.RestHighLevelClient;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ES 客户端连接池
 *
 * @author z
 */
public class EsRestClientPool {
    private static ConcurrentHashMap<RestHighLevelClient, String> connectionPool = new ConcurrentHashMap<>();
    private static int CORE_POOL_SIZE;
    private static int MAXIMUM_POOL_SIZE;
    private static String CLIENT_IPS;
    private static int ES_HTTP_PORT;
    private static String USERNAME;
    private static String PASSWORD;

    public static void init(String clientIps,int esHttpPort, String username, String password,int corePoolSize,int maximumPoolSize) {
        CLIENT_IPS = clientIps;
        ES_HTTP_PORT = esHttpPort;
        USERNAME = username;
        PASSWORD = password;
        CORE_POOL_SIZE = corePoolSize;
        MAXIMUM_POOL_SIZE = maximumPoolSize;
        for (int i = 0; i < CORE_POOL_SIZE; i++) {
            connectionPool.put(EsRestClientFactory.getRestHighLevelClient(clientIps, esHttpPort, username, password), "idle");
        }
    }

    // TODO 多个请求共用一个连接，其中一个请求异常，是否两个都会受到影响
    public static synchronized RestHighLevelClient getClient() {
        for (Map.Entry<RestHighLevelClient, String> entry : connectionPool.entrySet()) {
            if ("idle".equals(entry.getValue())) {
                connectionPool.put(entry.getKey(),"busy");
                return entry.getKey();
            }
        }
        if (connectionPool.size() < MAXIMUM_POOL_SIZE) {
            RestHighLevelClient client = EsRestClientFactory.getRestHighLevelClient(CLIENT_IPS, ES_HTTP_PORT, USERNAME, PASSWORD);
            connectionPool.put(client, "busy");
            return client;
        } else {
            return null;
        }
    }

    public static synchronized void recycleClient(RestHighLevelClient client) {
        if (connectionPool.containsKey(client)) {
            connectionPool.put(client, "idle");
        }
    }

    public static Set<Map.Entry<RestHighLevelClient,String>> getIdleClients() {
        return EsRestClientPool.connectionPool.entrySet();
    }
}
