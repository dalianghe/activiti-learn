<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>请假单</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8" />
    <link rel="stylesheet" href="/plugin/layui/css/layui.css">
    <link rel="stylesheet" href="/plugin/x-admin/css/xadmin.css">
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="/plugin/layui/layui.all.js" charset="utf-8"></script>
    <!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<div class="x-body">
    <form class="layui-form" style="margin-left: 20px;">
        <div style="width:97%;height:410px;overflow: auto;">
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">请假单</legend>
                </fieldset>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">
                    <span class="x-red">*</span>开始日期
                </label>
                <div class="layui-input-inline">
                    <input type="text"  id="startDate" name="startDate" autocomplete="off" class="layui-input" readonly>
                </div>
                <label class="layui-form-label">
                    <span class="x-red">*</span>结束日期
                </label>
                <div class="layui-input-inline">
                    <input type="text"  id="endDate" name="endDate" autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">
                    <span class="x-red">*</span>请假天数
                </label>
                <div class="layui-input-inline">
                    <input type="text"  id="days" name="days" autocomplete="off" class="layui-input">
                </div>
                <label class="layui-form-label">
                    <span class="x-red">*</span>请假类型
                </label>
                <div class="layui-input-inline">
                    <select name="vacationType">
                        <option value=""></option>
                        <option value="0">事假</option>
                        <option value="1">病假</option>
                        <option value="2">婚假</option>
                        <option value="3">产假</option>
                        <option value="4">调休</option>
                    </select>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">
                    <span class="x-red">*</span>请假原因
                </label>
                <div class="layui-input-block">
                    <textarea name="reason" placeholder="请输入请假原因" class="layui-textarea"></textarea>
                </div>
            </div>
            <div style="height: 60px"></div>
        </div>
        <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6;position: fixed;bottom: 1px;margin-left:-20px;">
            <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
                <button id="submitButton" class="layui-btn layui-btn-normal" lay-filter="add" lay-submit="">
                    <i class="layui-icon">&#xe605;</i>提交
                </button>
                <button  class="layui-btn layui-btn-primary" id="close">
                    <i class="layui-icon">&#x1006;</i>取消
                </button>
            </div>
        </div>
    </form>
</div>
<script>
    layui.use(['form','layer','laydate'], function(){
        $ = layui.jquery;
        var form = layui.form
                ,layer = layui.layer
                ,laydate = layui.laydate;

        laydate.render({
            elem: '#startDate' //指定元素
            //,range: '~'
        });
        laydate.render({
            elem: '#endDate' //指定元素
        });

        $('#close').click(function(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        });

        //自定义验证规则
        form.verify({
            processName: function(value){
                if(value.trim()==""){
                    return "流程名称不能为空";
                }
                if(!/(.+){3,12}$/.test(value)){
                    return "用户名必须3到12位";
                }
            }
        });

        form.on('submit(add)', function(data){
            console.log(data.field);
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);

            $.ajax({
                url: 'vacationApply',  // 服务url
                type: 'post', // post请求
                data:data.field, // 参数
                async:false,  // 同步请求
                dataType: "json",  // 返回json
                //traditional: true, // 防止深度序列化
                success:function(data){
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                    window.parent.layui.table.reload('applyList');
                    window.top.layer.msg(data.msg,{icon:6,offset: 'rb',area:['120px','80px'],anim:2});
                },
                error:function(){
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                    window.top.layer.msg('请求失败',{icon:5,offset: 'rb',area:['120px','80px'],anim:2});
                }
            })
        });
        form.render();
    });
</script>
</body>

</html>