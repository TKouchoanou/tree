package malo.bloc.tree.conf.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Getter
@Setter
public class ElasticsearchProperties {
    @Setter @Getter
    public static class HttpHost {
        private String hostname;
        private int port;
        private String scheme;
    }
    HttpHost hosts;
    private int numberOfShard;
    private int numberOfReplica;
    private int delayMillisBeforeReindexation;
}
