package com.hushao.schoolblog.service;

import com.hushao.schoolblog.domain.Catalog;
import com.hushao.schoolblog.domain.Province;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/23
 */
public interface ProvinceService {
    /**
     * 查询省份
     * @return
     * @throws Exception
     */
    List<Province> listPrivinces() throws Exception;
    /**
     * 添加省份
     * @param province
     * @return
     * @throws Exception
     */
    Province saveProvince(Province province) throws Exception;


}
