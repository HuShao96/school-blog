package com.hushao.schoolblog.domain.es;

import com.hushao.schoolblog.domain.Blog;
import org.elasticsearch.action.fieldstats.FieldStats;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * EsBlog 文档类
 * @author hushao
 * @date 2017/10/30
 */
@Document(indexName = "blog",type = "blog")
public class EsBlog implements Serializable{
    @Id
    private String id;

    // @Field(index = FieldIndex.not_analyzed):不作全文检索
    @Field(index = FieldIndex.not_analyzed)
    private Long blogId;

    private String title;

    private String content;

    @Field(index = FieldIndex.not_analyzed)
    private String username;

    @Field(index = FieldIndex.not_analyzed)
    private String avatar;

    @Field(index = FieldIndex.not_analyzed)
    private String catalog;

    @Field(index = FieldIndex.not_analyzed)
    private Timestamp createTime;

    @Field(index = FieldIndex.not_analyzed)
    private Integer readSize=0;

    @Field(index = FieldIndex.not_analyzed)
    private Integer commentSize=0;

    @Field(index = FieldIndex.not_analyzed)
    private Integer voteSize=0;
    protected EsBlog(){}

    public EsBlog(Long blogId, String title, String content, String username, String avatar, Timestamp createTime, Integer readSize, Integer commentSize, Integer voteSize) {
        this.blogId = blogId;
        this.title = title;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.createTime = createTime;
        this.readSize = readSize;
        this.commentSize = commentSize;
        this.voteSize = voteSize;
    }
    public EsBlog(Blog blog){
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.catalog=blog.getCatalog().getCatalogName();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
    }
    public void update(Blog blog){
        this.blogId = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.username = blog.getUser().getUsername();
        this.avatar = blog.getUser().getAvatar();
        this.createTime = blog.getCreateTime();
        this.catalog=blog.getCatalog().getCatalogName();
        this.readSize = blog.getReadSize();
        this.commentSize = blog.getCommentSize();
        this.voteSize = blog.getVoteSize();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Integer voteSize) {
        this.voteSize = voteSize;
    }
}
