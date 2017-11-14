$(function () {
    var pictureApi;
    // 获取编辑分类图片的界面
    $(".blog-content-container").on("click",".catalog-edit-picture", function () {
        pictureApi = "/catalogs/"+$(this).attr("catalogId")+"/picture";
        axios({
            url: pictureApi
        }).then(function (response) {
            $("#pictureFormContainer").html(response.data);
        }).catch(function () {
            toastr.error("error!");
        });
    });
    /**
     * 将以base64的图片url数据转换为Blob
     * @param urlData
     *            用url方式表示的base64图片数据
     */
    function convertBase64UrlToBlob(urlData){
        var bytes=window.atob(urlData.split(',')[1]);        //去掉url的头，并转换为byte
        //处理异常,将ascii码小于0的转换为大于0
        var ab = new ArrayBuffer(bytes.length);
        var ia = new Uint8Array(ab);
        for (var i = 0; i < bytes.length; i++) {
            ia[i] = bytes.charCodeAt(i);
        }
        return new Blob( [ab] , {type : 'image/png'});
    }

    // 提交分类的图片数据
    $("#submitEditPicture").on("click", function () {
        var form = $('#pictureformid')[0];
        var formData = new FormData(form);   //这里连带form里的其他参数也一起提交了,如果不需要提交其他参数可以直接FormData无参数的构造函数
        var base64Codes = $(".cropImg > img").attr("src");
        formData.append("file",convertBase64UrlToBlob(base64Codes));  //append函数的第一个参数是后台获取数据的参数名,和html标签的input的name属性功能相同
        axios({
            method:'POST',
            url:'http://localhost:8081/upload',
            data: formData
        }).then(function (response) {
            var catalogPicture=response.data;
            var csrfToken = $("meta[name='_csrf']").attr("content");
            axios({
                method:'POST',
                url: pictureApi,
                headers:{'X-CSRF-TOKEN':csrfToken,
                    'Content-Type':"application/json; charset=utf-8"},
                params:{catalogId:Number($(".catalog-edit-picture").attr("catalogId")),catalogPicture:catalogPicture}
            }).then(function (response) {
                if (response.data.success) {
                    // 成功后，置换头像图片
                    $(".catalog-picture").attr("src", response.data.body);
                } else {
                    toastr.error("error!"+response.data.message);
                }
            }).catch(function () {
                toastr.error("error!");
            });
        }).catch(function () {
            toastr.error("error!");
        });
    });
});