$(function() {

    var _pageSize; // 存储用于搜索

    // 根据用户名、页面索引、页面大小获取用户列表
    function getBlogsByName(pageIndex, pageSize) {
        axios({
            url:"/blogs",
            params:{async:true,
            pageIndex:pageIndex,
            pageSize:pageSize,
            keyword:$("#indexkeyword").val()}
        }).then(function (response) {
            $("#mainContainer").html(response.data);
            var keyword = $("#indexkeyword").val();

            // 如果是分类查询，则取消最新、最热选中样式
            if (keyword.length > 0) {
                $(".nav-item .nav-link").removeClass("active");
            }
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
    $("#indexsearch").click(function() {
        getBlogsByName(0, _pageSize);
    });

    // 最新\最热切换事件
    $(".nav-item .nav-link").click(function() {

        var blogUrl = $(this).attr("url");

        // 先移除其他的点击样式，再添加当前的点击样式
        $(".nav-item .nav-link").removeClass("active");
        $(this).addClass("active");

        // 加载其他模块的页面到右侧工作区
        axios({
            url:blogUrl+'&async=true'
        }).then(function (response) {
            $("#mainContainer").html(response.data);
        }).catch(function () {
            toastr.error("error!");
        });
        // 清空搜索框内容
        $("#indexkeyword").val('');
    });


});