$(function () {
    //搜索
    $(".blog-menu .list-group-item").click(function () {
        var url=$(this).attr("url");
        //先移除其他的点击样式，再添加当前的点击样式
        $(".blog-menu .list-group-item").removeClass("active");
        $(this).addClass("active");
        //加载其他模块的页面到右侧工作区
        axios({
            method:'get',
            url:url
        }).then(function (response) {
            $("#rightContainer").html(response.data);
        }).catch(function (error) {
           toastr.error(error);
        });


    });
    //默认选中第一个菜单项
    $(".blog-menu .list-group-item:first").trigger("click");
});