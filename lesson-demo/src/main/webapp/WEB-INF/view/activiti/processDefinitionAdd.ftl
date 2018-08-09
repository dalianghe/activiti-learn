<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>流程发布</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8" />
    <link rel="stylesheet" href="/plugin/layui/css/layui.css">
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
    <form class="layui-form layui-form-pane" style="margin-left: 20px;">
        <div style="width:97%;height:400px;overflow: auto;">
            <#--<div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">流程基本信息</legend>
                </fieldset>
            </div>
            <div class="layui-form-item">
                <label for="uname" class="layui-form-label">
                    <span class="x-red">*</span>流程名称
                </label>
                <div class="layui-input-inline">
                    <input type="text"  id="processName" name="processName"  lay-verify="processName" autocomplete="off" class="layui-input">
                </div>
            </div>-->
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">流程文件上传</legend>
                </fieldset>
                <div class="layui-input-inline">
                    <div class="layui-upload-drag" style="margin-left:10%;" id="test10">
                        <i style="font-size:30px;" class="layui-icon"></i>
                        <p style="font-size: 10px">点击上传，或将文件拖拽到此处</p>
                    </div>
                </div>
            </div>
            <div style="height: 60px"></div>
        </div>
        <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6;position: fixed;bottom: 1px;margin-left:-20px;">
            <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
                <#--<button id="submitButton" class="layui-btn layui-btn-normal" lay-filter="add" lay-submit="">
                    增加
                </button>-->
                <button  class="layui-btn layui-btn-primary" id="close">
                    取消
                </button>
            </div>
        </div>
    </form>
</div>
<script>
    layui.use(['form','layer','upload'], function(){
        $ = layui.jquery;
        var form = layui.form
                ,layer = layui.layer,
                upload = layui.upload;
        upload.render({
            elem: '#test10' // 绑定元素
            ,url: 'upload'  // 请求地址
            ,method: 'post'  // 请求方法类型
            ,accept: 'file'  // 允许上传文件
            ,exts: 'zip' // 只允许上传zip文件
            ,before: function(obj){
                layer.load();
            }
            ,done: function(res){
                if(res.code == 200){
                    layer.msg(res.msg,{icon: 6,anim: 6});
                    window.parent.layui.table.reload('processDefinitionList');
                }else{
                    layer.msg(res.msg,{icon: 5,anim: 6});
                }
                layer.closeAll('loading');
            }
            ,error: function(index, upload){
                layer.closeAll('loading');
            }
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
            console.log(data);
        });
        form.render();
    });
</script>
</body>

</html>