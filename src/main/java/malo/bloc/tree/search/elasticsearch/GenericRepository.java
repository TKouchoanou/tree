package malo.bloc.tree.search.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import co.elastic.clients.elasticsearch.core.search.SourceFilter;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import malo.bloc.tree.search.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Log4j2
@Setter
@Getter
public abstract class GenericRepository<T,ID> implements SearchRepository<T, ID>  {
    private static final  int QUERY_SIZE = 1000;
    @Autowired
    ElasticsearchClient escli;
    Resource resourceFile;

     abstract String getBeanId(T bean);
    protected abstract Class<T> getBeanClass();
     abstract String getIndexName();
     abstract String getAliasName();
     abstract String getRelativePath();
     abstract <S extends T> Iterable<S> getAllBeans();

    @Override
    public Iterable<T> findAll(Sort sort) {
        SearchResponse sr= null;
        String indexName= this.getIndexName();
        List<SortOptions> sortOptionsList = this.sortToEsSortOptionsList(sort);
        Query queryMatchAll = this.matchAllEmpty();
        SearchRequest req = SearchRequest.of(s -> s
                .index(indexName)
                .query(queryMatchAll)
                .sort(sortOptionsList));

        try {
            sr = escli.search(req,getBeanClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sr.hits().hits();
    }

    @Override
    public Page <T> findAll(Pageable pageable) {
        String indexName= this.getIndexName();
        Sort sort = pageable.getSortOr(Sort.unsorted());
        List<SortOptions> sortOptionsList = this.sortToEsSortOptionsList(sort);
        Query queryMatchAll = this.matchAllEmpty();

        SearchRequest req = SearchRequest.of(s -> s
                .index(indexName)
                .query(queryMatchAll)
                .from(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .sort(sortOptionsList));

        SearchResponse sr= null;
        try {
            sr = escli.search(req, getBeanClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
       return new PageImpl<T>(sr.hits().hits(),pageable,sr.hits().total().value());
    }

    @Override
    public <S extends T> S save(S entity) {
        String indexName=this.getIndexName();
        String id = this.getBeanId(entity).isEmpty()?null:this.getBeanId(entity);
        IndexRequest<JsonData> req;
        req = IndexRequest.of(b -> b
                .index(indexName)
                .id(String.valueOf(id))
                .document(JsonData.of(entity))
        );
        IndexResponse ir= null;
        try {
            ir = escli.index(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
        S savedEntity = (S) findById((ID) ir.id()).get();

        return savedEntity;
    }

    @SneakyThrows
    @Override
    public  <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        this.createIndex();
        log.info("Indexing in {} index: Fetching all beans for indexation", getIndexName());
        List<S> allEntity = StreamSupport.stream(entities.spliterator(),false).collect(Collectors.toList());

            if (!allEntity.isEmpty()) {
                for (Object batchBeans : Lists.partition(allEntity, QUERY_SIZE)) {
                    BulkRequest bk = this.bulkRequest((List<Object>)batchBeans);
                     escli.bulk(bk);
                }
            }
        log.info("Indexing {}: Inserting all beans in the index ", getIndexName());
     return (Iterable<S>) this.findAll();
    }

    @SneakyThrows
    @Override
    public  <S extends T> Iterable<S> saveAll() {
       return (Iterable<S>) this.saveAll(this.getAllBeans());
    }

    public boolean createIndex() throws IOException {

        InputStream mappings = resolveMapping();
        String aliasName= this.getAliasName()!=null?this.getAliasName():"";
        String indexName= this.getIndexName();

        if(this.existIndex(indexName)){
            this.deleteIndex(indexName);
        }

        if(mappings!=null && !aliasName.isEmpty()){
          return   this.createIndex(indexName,aliasName,mappings);
        }
       if(mappings!=null){
           return this.createIndex(indexName,mappings);
       }
        if(!aliasName.isEmpty()){
            return this.createIndex(indexName,aliasName);
        }
        log.info("Creating index ", getIndexName());
        return this.createIndex(indexName);
    }

    @Override
    public Optional<T>  findById(ID id) {
        String indexName=this.getIndexName();
        GetRequest req = GetRequest.of(g->g
                .index(indexName)
                .id((String) id));
        GetResponse<Object> gr= null;
        try {
            gr = escli.get(req, (Class<Object>) getBeanClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gr!=null && gr.source()!=null ?(Optional<T>) Optional.of(gr.source()):Optional.empty();
    }

    @SneakyThrows
    @Override
    public boolean existsById(ID id) {
        ExistsRequest es = ExistsRequest.of(e->e.index(getIndexName()).id((String) id));
        BooleanResponse esr= escli.exists(es);
        return esr.value();
    }

    @Override
    public Iterable<T> findAll() {
        SearchResponse sr= null;
        String indexName= this.getIndexName();
        Query queryMatchAll = this.matchAllEmpty();
        SearchRequest req = SearchRequest.of(s -> s
                .index(indexName)
                .query(queryMatchAll));

        try {
            sr = escli.search(req,getBeanClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sr.hits().hits();
    }

    @Override
    public Iterable <T> findAllById(Iterable<ID> iterable) {
        SearchResponse sr= null;
        String indexName= this.getIndexName();
        List<FieldValue> fieldValueList= StreamSupport.stream(iterable.spliterator(),false).map(id ->FieldValue.of(flv->flv.stringValue((String) id))).collect(Collectors.toList());
        TermsQueryField termsQueryField= TermsQueryField.of(tf->tf.value(fieldValueList));
        Query queryTerms =Query.of(q->q.terms(t->t.field("_id").terms(termsQueryField)));
        SearchRequest req = SearchRequest.of(s -> s
                .index(indexName)
                .query(queryTerms));

        try {
            sr = escli.search(req,getBeanClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sr.hits().hits();
    }



    @SneakyThrows
    @Override
    public long count() {
        String indexName= this.getIndexName();
        CountRequest countRequest= CountRequest.of(c->c.index(indexName));
        CountResponse cr = escli.count(countRequest);
        return cr.count();
    }

    @SneakyThrows
    @Override
    public void deleteById(ID id) {
        String indexName= this.getIndexName();
        DeleteRequest req = DeleteRequest.of(d->d.index(indexName).id((String) id));
        escli.delete(req);
    }

    @Override
    public void delete(T entity) {
        ID id =(ID) this.getBeanId(entity);
        this.deleteById(id);
    }

    @SneakyThrows
    @Override
    public void deleteAllById(Iterable<? extends ID>  iterable) {
        String indexName= this.getIndexName();
        List<FieldValue> fieldValueList= StreamSupport.stream(iterable.spliterator(),false).map(id ->FieldValue.of(flv->flv.stringValue((String) id))).collect(Collectors.toList());
        TermsQueryField termsQueryField= TermsQueryField.of(tf->tf.value(fieldValueList));
        Query queryTerms =Query.of(q->q.terms(t->t.field("_id").terms(termsQueryField)));
        DeleteByQueryRequest req = DeleteByQueryRequest.of(s -> s
                .index(indexName)
                .query(queryTerms));
             escli.deleteByQuery(req);
    }

    @Override
    public void deleteAll(Iterable<? extends T>  entities) {
        Iterable idList= StreamSupport.stream(entities.spliterator(),false).map(entity->this.getBeanId(entity)).collect(Collectors.toList());
        this.deleteAllById(idList);
    }

    @SneakyThrows
    @Override
    public void deleteAll() {
        String indexName= this.getIndexName();
        DeleteByQueryRequest req = DeleteByQueryRequest.of(s -> s
                .index(indexName)
                .query(this.matchAllEmpty()));
        escli.deleteByQuery(req);

    }

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

    public boolean createIndex(String indexName) throws IOException {

        CreateIndexResponse cir=  escli.indices().create(
                c ->c.index(indexName)
        );
        return Boolean.TRUE.equals(cir.acknowledged());
    }
    public boolean createIndex(String indexName, InputStream mapping) throws IOException {

        CreateIndexResponse cir=  escli.indices().create(
                c ->c.index(indexName)
                        .withJson(mapping)
        );
        return Boolean.TRUE.equals(cir.acknowledged());
    }
    private   BulkRequest bulkRequest(List<Object> beans){
        List<BulkOperation> ops = new ArrayList<>();
        List<Object> documents = beans;
        //(List) this.beanListToStringList(beans);
        for (Object document:documents) {
            String docId="bis-"+(documents.indexOf(document)+1);
            IndexOperation<Object> io= IndexOperation.of(i->i.index(this.getIndexName()).document(document).id(docId));
            BulkOperation bo = BulkOperation.of(b->b.index(io));
            ops.add(bo);
        }
        BulkRequest bulk =BulkRequest.of(b->b.operations(ops).refresh(Refresh.WaitFor));
        return  bulk;
    }

    public boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexResponse dir=  escli.indices().delete(d->d.index(indexName).allowNoIndices(true));
        return dir.acknowledged();
    }

    public boolean existIndex(String indexName) throws IOException {
        BooleanResponse br=  escli.indices().exists(e->e.index(indexName));
        return br.value();
    }


    private InputStream defaultMapping() throws IOException {
        return resourceFile!=null? resourceFile.getInputStream():null;
    }
    public  InputStream mapping(String relativePath) throws IOException {
        return loadManually(relativePath).getInputStream();
    }

    private Resource loadManually(String relativePath) {
        return  new ClassPathResource(relativePath);
    }

    private InputStream stringToInputStream(String initialString){
        return new ByteArrayInputStream(initialString.getBytes());
    }
    private SourceConfig SourceListToSourceConfig(List<String> sourceList){
        SourceFilter sourceFilter= SourceFilter.of(sf->sf.includes(sourceList));
        return SourceConfig.of(s->s.filter(sourceFilter));
    }

    private  Query matchAllEmpty(){
        InputStream emptyObjet= this.stringToInputStream("{}");
        return Query.of(q->q.matchAll(m->m.withJson(emptyObjet)));
    }

    private List<SortOptions> sortToEsSortOptionsList(Sort sort){
        List<SortOptions> sortOptionsList =new ArrayList<>();
        sort.iterator().forEachRemaining(s->{
            SortOptions sortOptions = null;
            if(s.isAscending()){
                sortOptions = SortOptions.of(so-> so.field(f -> f.field(s.getProperty()).order(SortOrder.Asc)));
            }else {
                sortOptions = SortOptions.of(so-> so.field(f -> f.field(s.getProperty()).order(SortOrder.Desc)));
            }
            sortOptionsList.add(sortOptions);

        });
        return sortOptionsList;
    }

    private InputStream resolveMapping() throws IOException {
        String relativePath = this.getRelativePath();
        if(relativePath.isEmpty()){
            try {
                return this.defaultMapping();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.mapping(relativePath);
    }


}
