/*JQuery写法
$(function () {
    $.ajax({
        url:"/mymvc/emp/searchAll",
        type:"post",
        data:{
        },
        success:function (resp) {
            //console.log(resp)
            let list=resp.result
            let temp="";
            for (let one of list){
                temp+=`
                    <tr>
                    <td>${one.empno}</td>
                    <td>${one.ename}</td>
                    <td>${one.job}</td>
                    <td>${one.mgr}</td>
                    <td>${one.hiredate}</td>
                    <td>${one.sal}</td>
                    <td>${one.comm}</td>
                    <td>${one.deptno}</td>
                    </tr>
                `
            }
            $("#emp tbody").append(temp)
        },
        error:function (e) {
            console.log(e);
        }
    })
})*/
//vue写法
let example =new Vue({
    el:"#example",
    data:{//只有写在data里面的数据才能带回给页面
        list:[],
        page:1,
        size:5,
        lastPage:false,
        pageNums:1,
        empnoList:[],
        empno:"",
        ename:"",
        deptno:"",
        deptList:[]

    },
    methods:{
        //ref 用于将数据带回给调用toPage方法的那个对象中
        toPage:function(page,size,ref){
            //let that = this toPage是一个普通方法，没有通过点击事件这种来调用 所以这里面this指代的是当前的方法而不是Vue对象
            let params=new URLSearchParams();
            params.append('page',page);
            params.append('size',size)
            console.log(ref.empno)
            console.log(ref.ename)
            console.log(ref.deptno)
            if (ref.empno != null || ref.empno != ""){
                params.append('empno',ref.empno)
            }
            if (ref.ename != null || ref.ename != ""){
                params.append('ename',ref.ename)
            }
            if (ref.deptno != null || ref.deptno != ""){
                params.append('deptno',ref.deptno)
            }
            axios.post("/mymvc/emp/searchAllByPage",params).then(function (resp){
                let list=resp.data.result
                //将从后台获取的数据给vue对象里面的list 带回给html（即带回给前端页面）
                if(list == null || list.length == 0){
                    ref.lastPage == true
                }else {
                    ref.list=list
                }
            }).catch(function (e) {
                alert("执行异常")
                console.log(e)
            })
        },
        //向后翻页 函数
        next:function () {
            let that = this
            if(that.lastPage == false && that.page < that.pageNums){
                that.page++;
                that.toPage(that.page,that.size,that)
            }

        },
        //往前翻页 函数
        prev:function () {
            let that = this
            if(that.page>1){
                that.lastPage=false
                that.page--
                that.toPage(that.page,that.size,that)
            }

        },
        first:function () {
            let that=this
            if(that.page>1){
                that.page=1
                that.Page=false
                that.toPage(that.page,that.size,that)
            }
        },
        last:function () {
            let that = this
            if(that.lastPage == false){
                that.lastPage=true
                that.page=that.pageNums
                that.toPage(that.page,that.size,that)
            }
        },
        enter:function (e) {
            let that = this
            if(e.keyCode == 13){
                if(/^[1-9]\d{0,}$/.test(that.page) == false){
                    alert("页数要求是正整数")
                }
                else if(that.page > that.pageNums || that.page < 1){
                    alert("页数范围错误")
                }
                else{
                    that.toPage(that.page,that.size,that)
                }
            }

        },
        go: function () {
            let that = this
            if (/^[1-9]\d{0,}$/.test(that.page) == false) {
                alert("页数要求是正整数")
            } else if (that.page > that.pageNums || that.page < 1) {
                alert("页数范围错误")
            } else {
                that.toPage(that.page, that.size, that)
            }
        },
        select:function (e,empno) {
            let that = this
            let tr = e.target.parentNode
            if(tr.getAttribute("class") != null){
                tr.removeAttribute("class")
                let i = that.empnoList.indexOf(empno)
                that.empnoList.splice(i,1)
            }else{
                tr.setAttribute("class","active")
                that.empnoList.push(empno)
            }
        },
        deleteEmp:function() {
            if(confirm("是否删除数据？")){
                let that = this
                let params = new URLSearchParams();
                //console.log(that.empnoList);
                params.append("empnoList",that.empnoList);
                axios.post("/mymvc/emp/deleteByEmpno",params).then(function (resp){
                    alert("删除了"+resp.data.rows+"条记录")
                    that.page = 1
                    that.toPage(that.page,that.size,that)
                    axios.post("/mymvc/emp/searchCount").then(function (resp) {
                        let count = resp.data.count
                        that.pageNums = Math.ceil(count*1.0/that.size)
                    }).catch(function (e) {
                        alert("执行异常");
                        console.log(e)
                    })
                }).catch(function (e) {
                    alert("执行异常")
                    console.log(e)
                })
            }
        },
        search:function () {
            let that = this
            that.page = 1
            that.toPage(that.page,that.size,that)
        }
    },

    mounted:function () {
        //这个this指代的是这个vue对象
        let that = this
        //将Vue对象传参给toPage方法 用去带回数据
        that.toPage(that.page,that.size,that)
        axios.post("/mymvc/emp/searchCount").then(function (resp) {
            let count = resp.data.count
            that.pageNums = Math.ceil(count*1.0/that.size)
        }).catch(function (e) {
            alert("执行异常");
            console.log(e)
        })
        axios.post("/mymvc/dept/searchAll").then(function (resp) {
            let list = resp.data.list
            that.deptList = list
        }).catch(function (e) {
            alert("执行异常")
            console.log(e)
        })
    }
})