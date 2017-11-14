package com.hushao.schoolblog.controller;

import com.hushao.schoolblog.domain.Province;
import com.hushao.schoolblog.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hushao
 * @date 2017/10/23
 */
@RestController
@RequestMapping("/provinces")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;
    @GetMapping
    public List<Province> listProvinces() throws Exception{
        List<Province> provinces=provinceService.listPrivinces();
        return provinces;
    }
}
