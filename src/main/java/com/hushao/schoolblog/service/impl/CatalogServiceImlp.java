package com.hushao.schoolblog.service.impl;

import com.hushao.schoolblog.domain.*;

import com.hushao.schoolblog.repository.CatalogRepository;
import com.hushao.schoolblog.repository.ProvinceRepository;
import com.hushao.schoolblog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/23
 */
@Service
public class CatalogServiceImlp implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public Page<Catalog> listCatalogByName(String catalogName,Pageable pageable) throws Exception{
        catalogName="%"+catalogName+"%";
        return catalogRepository.findByCatalogNameLike(catalogName, pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeCatalog(Long id) throws Exception{
        catalogRepository.delete(id);
    }

    @Override
    public Catalog getCatalogById(Long id) throws Exception{
        return catalogRepository.findOne(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Catalog saveCatalog(Long provinceId,Catalog catalog) throws Exception{
        Province province=provinceRepository.findOne(provinceId);
        List<Catalog> catalogs=province.getCatalogs();
        //判断分类是否存在
        boolean isExist =false;
        for (Catalog c:catalogs){
            if(c.getCatalogName().equals(catalog.getCatalogName())){
                isExist=true;
                break;
            }
        }
        if(isExist){
            throw new IllegalArgumentException("该分类已存在！");
        }else {
            catalog.setProvince(province);
        }
        return catalogRepository.save(catalog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createAttention(Long catalogId) throws Exception {
        //关注分类
        Catalog originalcatalog=this.getCatalogById(catalogId);
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //判断用户是否关注过
        boolean isExist =false;
        for (int i=0;i<originalcatalog.getAttentions().size();i++){
            if(originalcatalog.getAttentions().get(i).getUser().getUserId().equals(user.getUserId())){
                isExist=true;
                break;
            }
        }
        if(isExist){
            throw new IllegalArgumentException("该用户已关注过！");
        }else {
            Attention attention=new Attention(user,originalcatalog);
            List<Attention> attentions=originalcatalog.getAttentions();
            attentions.add(attention);
            originalcatalog.setAttentions(attentions);
        }
        catalogRepository.save(originalcatalog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeAttention(Long catalogId, Long attentionId) throws Exception {
        //取消关注
        Catalog originalCatalog=catalogRepository.findOne(catalogId);
           //判断用户是否点关注过
           for (int index=0; index <originalCatalog.getAttentions().size(); index ++ ) {
            if (originalCatalog.getAttentions().get(index).getAttentionId().equals(attentionId)) {
                List<Attention> attentions=originalCatalog.getAttentions();
                attentions.remove(index);
                originalCatalog.setAttentions(attentions);
                catalogRepository.save(originalCatalog);
                break;
            }
        }
    }

    @Override
    public void saveOrUpdateCatalog(Catalog originaCatalog) throws Exception {
        catalogRepository.save(originaCatalog);
    }

    @Override
    public List<Catalog> listCatalogHottestTop5() throws Exception {
        Sort sort=new Sort(Sort.Direction.DESC,"attentionSize","createTime");
        Pageable pageable=new PageRequest(0,5,sort);
        Page<Catalog> page=catalogRepository.findAll(pageable);
        return page.getContent();
    }




}
