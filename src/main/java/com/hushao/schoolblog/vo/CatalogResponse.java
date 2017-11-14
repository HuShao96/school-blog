package com.hushao.schoolblog.vo;

import java.io.Serializable;

/**
 * @author 930588706
 * @date 2017/11/10
 */
public class CatalogResponse implements Serializable {
    private Long catalogId;
    private String catalogName;
    private String isAuthority;

    public CatalogResponse(Long catalogId, String catalogName, String isAuthority) {
        this.catalogId = catalogId;
        this.catalogName = catalogName;
        this.isAuthority = isAuthority;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getIsAuthority() {
        return isAuthority;
    }

    public void setIsAuthority(String isAuthority) {
        this.isAuthority = isAuthority;
    }
}
