$(function () {
    $("#btn").click(function () {
        let username = $("#username").val();
        $.ajax({
            url:"http://127.0.0.1:8081/mymvc/demo/sayHello",
            type:"post",
            data:{
                "username":username
            },
            success:function (resp) {
                /*获取响应返回的值*/
                let temp=resp.result
                $("#result").text(temp)
            },
            error:function (e) {
                alert("执行异常")
                console.log(e)
            }
        })
    })
})