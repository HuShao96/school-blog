/*!
 * u main JS.
 *
 * @since:
 * @author http://weibo.com/2419467101/profile?is_all=1
 */
"use strict";
//# sourceURL=u.js
// DOM 加载完再执行
$(function() {
    var _pageSize; // 存储用于搜索
    var catalogId;//分类Id
    // 根据用户名、页面索引、页面大小获取用户列表
    function getBlogsByName(pageIndex, pageSize) {
        axios({
            url: "/u/"+  username  +"/blogs",
            herders:{'contentType':'application/json'},
            params:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword":$("#keyword").val(),
                "catalogId":catalogId
            }
        }).then(function (response) {
            $("#mainContainer").html(response.data);
        }).catch(function (error) {
            toastr.error("error!");
        });

    }

    // 分页
    $.tbpage("#mainContainer", function (pageIndex, pageSize) {
        getBlogsByName(pageIndex, pageSize);
        _pageSize = pageSize;
    });

    // 关键字搜索
    $("#searchBlogs").click(function() {
        getBlogsByName(0, _pageSize);
    });

    // 最新\最热切换事件
    $(".nav-item .nav-link").click(function() {
        var url = $(this).attr("url");

        // 先移除其他的点击样式，再添加当前的点击样式
        $(".nav-item .nav-link").removeClass("active");
        $(this).addClass("active");

        // 加载其他模块的页面到右侧工作区
        axios({
            url: url+'&async=true'
        }).then(function (response) {
            $("#mainContainer").html(response.data);
        }).catch(function (error) {
            toastr.error("error!");
        });

        // 清空搜索框内容
        $("#keyword").val('');
    });
    // 根据分类查询
    $(".blog-content-container").on("click",".list-group-item", function () {
        catalogId = $(this).attr('catalogId');
        getBlogsByName(0, _pageSize);
    });
    // 获取分类列表
    function getCatalogs(username) {
        axios({
            url: '/u/'+username+'/catalogs'
        }).then(function (response) {
            $("#catalogMain").html(response.data);
        }).catch(function (error) {
            toastr.error("error!");
        });
    }
    getCatalogs(username);
});