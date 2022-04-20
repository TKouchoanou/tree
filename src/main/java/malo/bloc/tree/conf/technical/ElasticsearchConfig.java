package malo.bloc.tree.conf.technical;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import malo.bloc.tree.conf.properties.ElasticsearchProperties;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Autowired
    private ElasticsearchProperties elasticsearchProperties;
    @Bean
    public ElasticsearchClient restHighLevelClient() {
      // put content-type header for old elastic-search version (6*.)
        Header[] defaultHeaders = new Header[]{new BasicHeader("Content-Type", "application/json")};
        RestClient restClient = RestClient.builder(
                new HttpHost(elasticsearchProperties.getHosts().getHostname(), elasticsearchProperties.getHosts().getPort()))
                .setDefaultHeaders(defaultHeaders)
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

         // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        return client;
    }
}
