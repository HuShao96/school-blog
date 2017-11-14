package com.hushao.schoolblog.service.impl;
import com.hushao.schoolblog.domain.User;
import com.hushao.schoolblog.domain.es.EsBlog;
import com.hushao.schoolblog.repository.es.EsBlogRepository;
import com.hushao.schoolblog.service.EsBlogService;
import com.hushao.schoolblog.service.UserService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

/**
 * @author hushao
 * @date 2017/10/30
 */
@Service
public class EsBlogServiceImpl implements EsBlogService {
    @Autowired
    private EsBlogRepository esBlogRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private UserService userService;
    private static final Pageable TOP_5_PAGEABLE = new PageRequest(0, 5);
    private static final String EMPTY_KEYWORD = "";
    @Override
    public void removeEsBlog(String id) throws Exception {
        esBlogRepository.delete(id);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public EsBlog updateEsBlog(EsBlog esBlog) throws Exception{
        return esBlogRepository.save(esBlog);
    }

    @Override
    public EsBlog getEsBlogByBlogId(Long blogId){
        return esBlogRepository.findByBlogId(blogId);
    }

    @Override
    public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) throws SearchParseException {
        Page<EsBlog> pages=null;
        Sort sort=new Sort(Sort.Direction.DESC,"createTime");
        if(pageable.getSort()==null){
            pageable=new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        pages=esBlogRepository.findDistinctEsBlogByTitleContainingOrContentContaining(keyword,keyword,pageable);
        return pages;
    }

    @Override
    public Page<EsBlog> listHottestEsBlogs(String keyword, Pageable pageable) throws SearchParseException{
        Sort sort=new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
        if(pageable.getSort()==null){
            pageable=new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),sort);
        }
        return esBlogRepository.findDistinctEsBlogByTitleContainingOrContentContaining(keyword,keyword,pageable);
    }

    @Override
    public Page<EsBlog> listEsBlogs(Pageable pageable) {
        return esBlogRepository.findAll(pageable);
    }

    @Override
    public List<EsBlog> listTop5NewestEsBlogs() {
        Page<EsBlog> page=this.listNewestEsBlogs(EMPTY_KEYWORD,TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<EsBlog> listTop5HottestEsBlogs() {
        Page<EsBlog> page=this.listHottestEsBlogs(EMPTY_KEYWORD,TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<User> listTop10Users() {
        List<String> usernamelist = new ArrayList<>();
        // given
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSearchType(SearchType.QUERY_THEN_FETCH)
                .withIndices("blog").withTypes("blog")
                .addAggregation(terms("users").field("username").order(Terms.Order.count(false)).size(10))
                .build();
        // when
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        StringTerms modelTerms =  (StringTerms)aggregations.asMap().get("users");

        Iterator<Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Bucket actiontypeBucket = modelBucketIt.next();
            String username = actiontypeBucket.getKey().toString();
            usernamelist.add(username);
        }
        List<User> list = userService.listUsersByUsernames(usernamelist);

        return list;
    }
}
