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
            <div class="layui-form-item">
                <fieldset class="layui-elem-field layui-field-title" style="margin-top: 10px;">
                    <legend style="font-size:16px;">流程跟踪记录</legend>
                </fieldset>
                <table id="processHistoryList" class="layui-hide" lay-filter="processHistory"></table>
            </div>
            <div style="height: 60px"></div>
        </div>
        <div style="width: 100%;height: 55px;background-color: white;border-top:1px solid #e6e6e6;position: fixed;bottom: 1px;margin-left:-20px;">
            <div class="layui-form-item" style=" float: right;margin-right: 30px;margin-top: 8px">
                <button  class="layui-btn layui-btn-primary" id="close">
                    <i class="layui-icon">&#x1006;</i>关闭
                </button>
            </div>
        </div>
    </form>
</div>
<#setting classic_compatible=true>
<script type="text/html" id="indexTpl">
    {{d.LAY_TABLE_INDEX+1}}
</script>
<script>

    layui.use(['layer','table'], function(){
        var $ = layui.jquery;
        var table = layui.table;

        $('#close').click(function(){
            var index = parent.layer.getFrameIndex(window.name);
            parent.layer.close(index);
        });

        //方法级渲染
        table.render({
            id: 'processHistoryList',
            elem: '#processHistoryList'
            , url: '/activiti/processHistoryListData?processInstanceId=${processInstanceId}'
            , cols: [[
                {field: 'id', title: '序号', width: '8%', templet: '#indexTpl'}
                , {field: 'name', title: '节点名称', width: '16%'}
                , {field: 'assignee', title: '处理人', width: '12%'}
                , {field: 'startTime', title: '接收时间', width: '22%'}
                , {field: 'endTime', title: '完成时间', width: '22%'}
                , {field: 'duration', title: '办理时长（秒）', width: '20%'}
            ]]
            , page: false,
            height: 'full'
        });

    });
</script>
</body>

</html>