package com.hushao.schoolblog.domain;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 省份
 * @author hushao
 * @date 2017/10/23
 */
@Entity
public class Province implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long provinceId;
    @NotEmpty(message = "省份名称不能为空！")

    @Size(min = 2,max = 20)
    @Column(nullable = false,unique = true)
    private String provinceName;

    @OneToMany(mappedBy = "province",fetch = FetchType.LAZY,cascade = {CascadeType.ALL})
    private List<Catalog> catalogs;

    protected Province(){}

    public Province(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }
}
