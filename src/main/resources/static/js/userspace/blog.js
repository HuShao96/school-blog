/*!
 * blog.html 页面脚本.
 *
 *
 * @author http://weibo.com/2419467101/profile?is_all=1
 */
"use strict";
//# sourceURL=blog.js

// DOM 加载完再执行
$(function() {
    // 处理删除博客事件
    $(".blog-content-container").on("click",".blog-delete-blog", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        axios({
            url: $(this).attr("blogUrl"),
            method: 'DELETE',
            params:{catalogId:catalogId},
            headers:{'X-CSRF-TOKEN':csrfToken}
        }).then(function (response) {
            if (response.data.success) {
                // 成功后，重定向
                window.location = response.data.body;
            } else {
                toastr.error(response.data.message);
            }
        }).catch(function () {
            toastr.error("error!");
        });

    });

    // 获取评论列表
    function getCommnet(blogId) {
        axios({
            url: '/comments',
            params:{blogId:blogId,catalogId:catalogId}
        }).then(function (response) {
            $("#mainContainer").html(response.data);
        }).catch(function () {
            toastr.error("error!");
        });
    }

    // 提交评论
    $(".blog-content-container").on("click","#submitComment", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        axios({
            url: '/comments',
            method:'POST',
            params:{blogId:blogId, commentContent:$('#commentContent').val()},
            headers:{'X-CSRF-TOKEN':csrfToken}
        }).then(function (response) {
            if (response.data.success) {
                // 清空评论框
                $('#commentContent').val('');
                getBlogToAsyn();
                toastr.success(response.data.message);
            } else {
                toastr.error(response.data.message);
            }
        }).catch(function () {
            toastr.error("error!");
        })

    });

    //删除评论
    $(".blog-content-container").on("click",".blog-delete-comment", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        axios({
            url: '/comments/'+$(this).attr("commentId"),
            method:'DELETE',
            params:{
                blogId:blogId,
                catalogId:catalogId
            },
            headers:{'X-CSRF-TOKEN':csrfToken}
        }).then(function (response) {
            if (response.data.success) {
                getBlogToAsyn();
                toastr.info(response.data.message);
            } else {
                toastr.error(response.data.message);
            }
        }).catch(function () {
            toastr.error("error!");
        });
    });

    // 提交点赞
    $(".blog-content-container").on("click","#submitVote", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        axios({
            url: '/votes',
            method:'POST',
            params:{blogId:blogId},
            headers:{'X-CSRF-TOKEN':csrfToken}
        }).then(function (response) {
            if (response.data.success) {
                // 成功后，重定向
                getBlogToAsyn();
                toastr.success(response.data.message);
            } else {
                toastr.error(response.data.message);
            }
        }).catch(function () {
            toastr.error("error!");
        });

    });

    // 取消点赞
    $(".blog-content-container").on("click","#cancelVote", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        axios({
            url: '/votes/'+$(this).attr('voteId')+'?blogId='+blogId,
            method:'DELETE',
            headers:{'X-CSRF-TOKEN':csrfToken}
        }).then(function (response) {
            if (response.data.success) {
                // 成功后
                getBlogToAsyn();
                toastr.info(response.data.message);
            } else {
                toastr.error(response.data.message);
            }
        }).catch(function () {
            toastr.error("error!");
        });

    });

    //异步获取博客
    function getBlogToAsyn(){
        axios({
            url:blogUrl,
            params:{async:true,catalogId:catalogId}
        }).then(function (response) {
            $(".blog-content-container").html(response.data);
            getCommnet(blogId);
        }).catch(function (error) {
            toastr.error("erro!");
        });
    }

    // 初始化 博客评论
    getCommnet(blogId);
});