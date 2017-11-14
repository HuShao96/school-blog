package com.hushao.schoolblog.service.impl;

import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.Province;
import com.hushao.schoolblog.repository.ProvinceRepository;
import com.hushao.schoolblog.service.CatalogService;
import com.hushao.schoolblog.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/23
 */
@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private CatalogService catalogService;

    @Override
    public List<Province> listPrivinces() throws Exception{
        return provinceRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Province saveProvince(Province province) throws Exception{
        return provinceRepository.save(province);
    }

}
