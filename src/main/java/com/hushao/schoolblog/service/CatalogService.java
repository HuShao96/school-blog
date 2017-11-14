package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author hushao
 * @date 2017/10/23
 */
public interface CatalogService {
    /**
     * 根据名称模糊查询
     * @param catalogName
     * @return
     * @throws Exception
     */
    Page<Catalog> listCatalogByName(String catalogName, Pageable pageable) throws Exception;

    /**
     * 删除分类
     * @param id
     * @throws Exception
     */
    void removeCatalog(Long id) throws Exception;

    /**
     * 根据id分类查询
     * @param id
     * @return
     * @throws Exception
     */
    Catalog getCatalogById(Long id) throws Exception;


    /**
     * 增加栏目
     * @param  provinceId
     * @param catalog
     * @return
     * @throws Exception
     */
    Catalog saveCatalog(Long provinceId,Catalog catalog) throws Exception;

    /**
     * 关注
     * @param catalogId
     * @throws Exception
     */
    void createAttention(Long catalogId) throws Exception;

    /**
     * 取消关注
     * @param catalogId
     * @param attentionId
     */
    void removeAttention(Long catalogId, Long attentionId)throws Exception;

    /**
     * 修改或添加
     * @param originaCatalog
     * @throws Exception
     */
    void saveOrUpdateCatalog(Catalog originaCatalog)throws Exception;

    /**
     * 最热分类前5
     * @return
     * @throws Exception
     */
    List<Catalog> listCatalogHottestTop5() throws Exception;

}
