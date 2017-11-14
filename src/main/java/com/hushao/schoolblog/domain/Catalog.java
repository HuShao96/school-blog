package com.hushao.schoolblog.domain;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 分类
 * @author hushao
 * @date 2017/10/23
 */
@Entity
public class Catalog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 图片
     */
    private String catalogPicture;

    @NotEmpty(message = "名称不能为空！")
    @Size(min = 2,max = 20)
    @Column(nullable = false)
    private String catalogName;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp createTime;

    //关注量
    @Column(name ="attentionSize")
    private Integer attentionSize=0;

    //所属省份
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="province_id")
    private Province province;

    //此分类的博客
    @OneToMany(mappedBy = "catalog",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Blog> blogs;

    //关注
    @OneToMany(mappedBy = "catalog",cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    private List<Attention> attentions;

    protected Catalog(){}

    public Catalog(String catalogName) {
        this.catalogName = catalogName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatalogPicture() {
        return catalogPicture;
    }

    public void setCatalogPicture(String catalogPicture) {
        this.catalogPicture = catalogPicture;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }


    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    public Integer getAttentionSize() {
        return attentionSize;
    }

    public void setAttentionSize(Integer attentionSize) {
        this.attentionSize = attentionSize;
    }

    public List<Attention> getAttentions() {
        return attentions;
    }

    public void setAttentions(List<Attention> attentions) {
        this.attentions = attentions;
        this.attentionSize=attentions.size();
    }
}
