package malo.bloc.tree.search.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;

import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.core.search.SourceFilter;
import co.elastic.clients.elasticsearch.core.search.TrackHits;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import malo.bloc.tree.search.bean.User;
import malo.bloc.tree.persistence.repository.UserRepository;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericRepositoryImpl{
    private static final  int QUERY_SIZE = 1000;
    @Autowired
    ElasticsearchClient escli;
    @Autowired
    UserRepository userRepository;

    @Value("classpath:elasticsearch/user-index.json")
    Resource resourceFile;

    ModelMapper mapper2 = new ModelMapper();

    JacksonJsonpMapper mapper = new JacksonJsonpMapper();


    public  InputStream defaultMapping() throws IOException {
        return resourceFile.getInputStream();
    }

    public  InputStream mapping(String relativePath) throws IOException {
        return loadManually(relativePath).getInputStream();
    }

    public Resource loadManually(String relativePath) {
        return  new ClassPathResource(relativePath);
    }

    public String getFileContentAsString(String relativePath) throws IOException {
            File resource;
        resource = new ClassPathResource(
                relativePath).getFile();

        String employees = new String(
                    Files.readAllBytes(resource.toPath()));
        return employees;
    }

    public FileReader getFileReader(String relativePath) throws IOException {
        File resource;
        resource = new ClassPathResource(
                relativePath).getFile();
        FileReader fileReader = new FileReader(resource);
        return fileReader;
    }

    public BufferedReader getBufferedReader(String relativePath) throws IOException {
        Resource resource;
        resource = new ClassPathResource(
                relativePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        return bufferedReader;
    }


    public InputStream stringToInputStream(String initialString){
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        return targetStream;
    }


    /**
     * acknowledged indique si l'index a été créé avec succès dans le cluster,
     * tandis que shards_acknowledged indique si le nombre requis de copies de partition
     * a été lancé pour chaque partition de l'index avant l'expiration du délai
     * @param indexName
     * @param aliasName
     * @param mapping
     * @return
     * @throws IOException
     */
    public boolean createIndex(String indexName, String aliasName, InputStream mapping) throws IOException {

        CreateIndexResponse cir=  escli.indices().create(
                c ->c.index(indexName)
                        .aliases(aliasName,a->a.isWriteIndex(true)).withJson(mapping)
                        );
        return cir.acknowledged();
    }

    public boolean createIndex(String indexName, String aliasName) throws IOException {

        CreateIndexResponse cir=  escli.indices().create(
                c ->c.index(indexName)
                        .aliases(aliasName,a->a.isWriteIndex(true))
        );
        return cir.acknowledged();
    }
    public boolean createIndex(String indexName, InputStream mapping) throws IOException {

        CreateIndexResponse cir=  escli.indices().create(
                c ->c.index(indexName)
                        .withJson(mapping)
        );
        return cir.acknowledged();
    }
    public boolean createLeftIndex(String indexName, String aliasName) throws IOException {

        CreateIndexResponse cir=  escli.indices().create(
                c ->c.index(indexName)
                        .aliases(aliasName,a->a.isWriteIndex(false))
        );
        return cir.acknowledged();
    }
    public boolean createLeftIndex(String indexName, String aliasName, InputStream mapping) throws IOException {

        CreateIndexResponse cir=  escli.indices().create(
                c ->c.index(indexName)
                        .aliases(aliasName,a->a.isWriteIndex(false)).withJson(mapping)
        );
        return cir.acknowledged();
    }

    /**
     * Si allowNoIndice est false, la requête renvoie une erreur si une expression générique,
     * un alias d'index ou une valeur _all ne cible que des index manquants ou fermés.
     * La valeur par défaut est true.
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexResponse dir=  escli.indices().delete(d->d.index(indexName).allowNoIndices(true));
        return dir.acknowledged();
    }

    public boolean existIndex(String indexName) throws IOException {
        BooleanResponse br=  escli.indices().exists(e->e.index(indexName));
        return br.value();
    }

    public boolean indexBean(Object bean,String indexName,String id) throws IOException {
        // operation type opType(OpType.Index)
        IndexRequest<JsonData> req;
        req = IndexRequest.of(b -> b
                .index(this.getIndexName())
                .id(String.valueOf(id))
                .document(JsonData.of(bean))
        );
        IndexResponse ir= escli.index(req);
        return ir.shards().successful().intValue()>0;
    }

    public Object getBean(String indexName, String beanId,List<String> sourceList,Class<Object> type) throws IOException {
        GetRequest req = GetRequest.of(g->g.index(indexName).id(beanId).sourceIncludes(sourceList));
        GetResponse<Object> gr= escli.get(req,type);
        return gr.source();
    }

    /**
     *  {
     *    "query": {
     *       "match_all": {}
     *              }
     *
     * @param indexName
     * @param type
     * @return
     * @throws IOException
     */
    public List<Object> searchAllBean(String indexName, Class<User> type) throws IOException {
        InputStream emptyObjet= this.stringToInputStream("{}");
        Query queryMatchAll = Query.of(q->q.matchAll(m->m.withJson(emptyObjet)));
        TrackHits trackHits = TrackHits.of(th->th.enabled(false));
        SearchRequest req = SearchRequest.of(s -> s.index(indexName).query(queryMatchAll).trackTotalHits(trackHits).size(50));
        SearchResponse sr= escli.search(req,type);
        return sr.hits().hits();
    }

    public List<Object> searchAllBean(String indexName,List<String> sourceList,Class<Object> type) throws IOException {
        InputStream emptyObjet= this.stringToInputStream("{}");
        Query queryMatchAll = Query.of(q->q.matchAll(m->m.withJson(emptyObjet)));
        SourceConfig sourceConf =this.SourceListToSourceConfig(sourceList);
        SearchRequest req = SearchRequest.of(s -> s.index(indexName).query(queryMatchAll).source(sourceConf));
        SearchResponse sr= escli.search(req,type);
        return sr.hits().hits();
    }

    protected long count(String indexName) throws IOException {
        CountRequest countRequest= CountRequest.of(c->c.index(indexName));
        CountResponse cr=escli.count(countRequest);
        return cr.count();
    }
    private SourceConfig SourceListToSourceConfig(List<String> sourceList){
        SourceFilter sourceFilter= SourceFilter.of(sf->sf.includes(sourceList));
        SourceConfig sourceConf= SourceConfig.of(s->s.filter(sourceFilter));
        return sourceConf;
    }

   public Stream<Object> esTest() throws IOException {
        boolean c = false;
        boolean d = false;
       if(this.existIndex(this.getIndexName())){
           d= this.deleteIndex(this.getIndexName());
       }
        if(!this.existIndex(this.getIndexName())){
            c=this.createIndex(getIndexName(),this.defaultMapping());
        }
       try {
           BulkResponse br;
           List allBeans = getAllBeans();
           if (allBeans != null && !allBeans.isEmpty()) {
               for (Object batchBeans : Lists.partition(allBeans, QUERY_SIZE)) {
                   BulkRequest bk = this.bulkRequest((List)batchBeans);
                 br= escli.bulk(bk);
               }
           }
           long count =this.count(this.getIndexName());
           return  this.searchAllBean(getIndexName(), User.class).stream();
           // generic case   return  this.searchAllBean(getIndexName(),(Class<Object>) GenericTypeResolver.resolveTypeArgument(User.class.getClass(),User.class)).stream();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return null;
   }

    /**
     * Refresh.WaitFor
     * Attend que les modifications apportées par la requête soient rendues visibles par un rafraîchissement avant de répondre.
     * Cela ne force pas une actualisation immédiate, mais attend qu'une actualisation se produise. La valeur par défaut c'est false
     * https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-refresh.html
     * @param beans
     * @return
     */
    private   BulkRequest bulkRequest(List<Object> beans){
      List<BulkOperation> ops = new ArrayList<>();
      List documents = beans;
              //(List) this.beanListToStringList(beans);
        for (Object document:documents) {
            String docId="bis-"+(documents.indexOf(document)+1);
            IndexOperation io= IndexOperation.of(i->i.index(this.getIndexName()).document(document).id(docId));
            BulkOperation bo = BulkOperation.of(b->b.index(io));
            ops.add(bo);
        }
        BulkRequest bulk =BulkRequest.of(b->b.operations(ops).refresh(Refresh.WaitFor));
       return  bulk;
    }

    private Collection beanListToStringList(Collection beans){
        List documents = new ArrayList<>();
        for (Object bean : beans) {
            try {
                documents.add(mapper.objectMapper().writeValueAsString(bean));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        return documents;
    }


    private List getAllBeans() {
        List<malo.bloc.tree.persistence.entity.User> all = userRepository.findAll();
        return StreamSupport.stream(all.spliterator(),false).map(u->mapper2.map(u,User.class)).collect(Collectors.toList());
    }

    public String getBeanId(String bean) {
        return null;
    }


    public String getBeanName(String bean) {
        return "user";
    }


    public String getIndexName() {
        return "usermalov";
    }

    public void makeManyIndexBean() throws IOException {
        List allBeans = getAllBeans();
        if (allBeans != null && !allBeans.isEmpty()) {
            int i=1;
            for (Object bean :allBeans ) {
               this.indexBean(bean,getIndexName(), String.valueOf(i));
               i++;
            }
        }
    }


    public String getIndexType() {
        return "bulk";
    }
}


