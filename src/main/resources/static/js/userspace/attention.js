$(function () {

    var _pageSize;// 存储用于搜索
    var catalogId=catalogid;//分类Id
    // 根据分类、页面索引、页面大小获取用户列表
    function getBlogsByName(pageIndex, pageSize) {
        axios({
            url: "/catalogs/"+  catalogId  +"/blogs",
            herders:{'contentType':'application/json'},
            params:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword":$("#keyword").val(),
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
    // 提交关注
    $(".blog-content-container").on("click","#submitAttention", function () {

        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        axios({
            url: '/attentions',
            method:'POST',
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

    // 取消关注
    $(".blog-content-container").on("click","#cancelAttention", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        axios({
            url:'/attentions/'+$(this).attr('AttentionId'),
            method:'DELETE',
            params:{catalogId:catalogId},
            headers:{'X-CSRF-TOKEN':csrfToken}
        }).then(function (response) {
            if (response.data.success) {
                toastr.info(response.data);
                // 成功后
                window.location = response.data.body;
            } else {
                toastr.error(response.data.message);
            }
        }).catch(function () {
            toastr.error("error!");
        });

    });
});