/*!
 * blogedit.html 页面脚本.
 *
 * @author http://weibo.com/2419467101/profile?is_all=1
 */
"use strict";
//# sourceURL=blogedit.js

// DOM 加载完再执行
$(function() {
    var E = window.wangEditor;
    var editor = new E('#editor');
   // editor.customConfig.uploadImgServer = fileServerUrl;
   // editor.customConfig.uploadFileName = 'file';
   // editor.customConfig.uploadImgMaxLength = 1;
   // editor.customConfig.withCredentials = true;
   //
   //  editor.customConfig.uploadImgHooks = {
   //      customInsert: function (insertImg, result, editor) {
   //          insertImg(result);
   //      }
   //  };
   //  editor.customConfig.customUploadImg = function (files, insert) {
   //      axios({
   //          url: fileServerUrl,
   //          method: 'POST',
   //          params:{file:files[0]}
   //      }).then(function (response) {
   //          alert(response);
   //          insert(response.data);
   //      });
   //  };

    editor.create();
    editor.txt.html(htmlContent);

    // 发布博客
    $("#submitBlog").click(function() {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        axios({
            url: '/u/'+ $(this).attr("userName") + '/blogs/edit',
            method:'POST',
            headers:{'X-CSRF-TOKEN':csrfToken,
                'Content-Type':"application/json; charset=utf-8"},
            data:JSON.stringify({"id":$('#id').val(),
                "title": $('#title').val(),
                "content": editor.txt.text(),
                "catalog":{"id":$('#catalogSelect').val()},
                "htmlContent":editor.txt.html()})
        }).then(function (response) {
            if (response.data.success) {
                // 成功后，重定向
                window.location = response.data.body;
            } else {
                toastr.error("error!"+response.data.message);
            }
        }).catch(function () {
            toastr.error("error!");
        });
    });

});