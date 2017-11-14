package com.hushao.schoolblog.repository;

import com.hushao.schoolblog.domain.Catalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * 分类
 * @author hushao
 * @date 2017/10/23
 */
public interface CatalogRepository extends JpaRepository<Catalog,Long> {

    /**
     * 根据分类名称模糊查询（分页）
     * @param catalogName
     * @param pageable
     * @return
     * @throws Exception
     */
    Page<Catalog>findByCatalogNameLike(String catalogName, Pageable pageable) throws Exception;

}
