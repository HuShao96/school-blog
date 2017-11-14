package com.hushao.schoolblog.repository.es;

import com.hushao.schoolblog.domain.es.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author hushao
 * @date 2017/10/30
 */
public interface EsBlogRepository  extends ElasticsearchRepository<EsBlog,String> {
    /**
     * 模糊查询（Distinct：去除重复）
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    Page<EsBlog> findDistinctEsBlogByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    /**
     * 根据Blog的id查询EsBlog
     * @param blogId
     * @return
     */
    EsBlog findByBlogId(Long blogId);
}
