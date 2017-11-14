package com.hushao.schoolblog.domain;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Blog实体
 * @author hushao
 */
@Entity
public class Blog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "标题不能为空！")
    @Size(min = 2,max = 50)
    @Column(nullable = false,length = 50)
    private String title;

    @Lob    //大对象。映射MySql的Long Text类型
    @Basic(fetch = FetchType.LAZY)  //懒加载
    @NotEmpty(message = "内容不能为空！")
    @Size(min=2)
    @Column(nullable = false)
    private String content;


    /**
     * fetch = FetchType.LAZY:懒加载
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty(message = "内容不能为空！")
    @Size(min = 2)
    @Column(nullable = false)
    private String htmlContent;

    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //CreationTimestamp  ：有数据库自动创建时间
    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createTime;

    //访问量
    @Column(name = "readSize")
    private Integer readSize=0;

    //评论量
    @Column(name ="commentSize")
    private Integer commentSize=0;

    //点赞量
    @Column(name ="voteSize")
    private Integer voteSize=0;

    //评论
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "blog_id")
    private List<Comment> comments;

    //点赞
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private List<Vote> votes;

    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;


    protected Blog(){}

    public Blog(String title,  String content) {
        this.title = title;

        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
        this.commentSize=this.comments.size();
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
        this.voteSize=this.votes.size();
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }


}
