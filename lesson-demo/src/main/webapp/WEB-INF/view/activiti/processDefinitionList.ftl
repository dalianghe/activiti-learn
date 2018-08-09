<#-- Created by IntelliJ IDEA.
 User: Administrator
 Date: 2017/12/6
 Time: 14:00
 To change this template use File | Settings | File Templates.
 流程定义管理-->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>流程定义管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8"/>
    <link rel="stylesheet" href="/plugin/layui/css/layui.css">
    <link rel="stylesheet" href="/plugin/lenos/main.css">
    <script type="text/javascript" src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="/plugin/layui/layui.all.js"
            charset="utf-8"></script>

</head>

<body>
<div class="lenos-search">
    <div class="select">
        流程key：
        <div class="layui-inline">
            <input class="layui-input" height="20px" id="key" autocomplete="off">
        </div>
        流程名称：
        <div class="layui-inline">
            <input class="layui-input" height="20px" id="name" autocomplete="off">
        </div>
        <button class="select-on layui-btn layui-btn-sm" data-type="select">
            <i class="layui-icon"></i>
        </button>
        <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;" data-type="reload">
            <i class="layui-icon">ဂ</i>
        </button>
    </div>

</div>
<div class="layui-col-md12" style="height:32px;margin-top:3px;margin-left: 8px;">
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-sm layui-btn-normal" data-type="add">
            <i class="layui-icon">&#xe608;</i>发布流程
        </button>
    </div>
</div>
<table id="processDefinitionList" class="layui-hide" lay-filter="processDefinition"></table>
<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs" lay-event="detail">流程图</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
<script>
    document.onkeydown = function (e) { // 回车提交表单
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which;
        if (code == 13) {
            $(".select .select-on").click();
        }
    }
    layui.use('table', function () {
        var table = layui.table;
        //方法级渲染
        table.render({
            id: 'processDefinitionList',
            elem: '#processDefinitionList'
            , url: 'processDefinitionListData'
            , cols: [[
                {checkbox: true, fixed: true, width: '5%'}
                , {field: 'id', title: '流程id', width: '15%'}
                , {field: 'key', title: '流程key', width: '20%'}
                , {field: 'name', title: '流程名称', width: '15%', template: '#switchTpl'}
                , {field: 'version', title: '版本', width: '15%'}
                , {field: 'createDate', title: '部署时间', width: '15%'}
                , {field: 'right', title: '操作', width: '15%', toolbar: "#barDemo"}
            ]]
            , page: true,
            height: 'full-74'
        });

        var $ = layui.$, active = {
            select: function () {
                var key = $('#key').val();
                var name = $('#name').val();
                table.reload('processDefinitionList', {
                    where: {
                        key: key,
                        name: name
                    }
                });
            },
            reload: function () {
                $('#key').val('');
                $('#name').val('');
                table.reload('processDefinitionList', {
                    where: {
                        key: null,
                        name: null
                    }
                });
            },
            add: function () {
                add('流程发布', 'processDefinitionAdd', 600, 450);
            }
        };

        //监听表格复选框选择
        table.on('checkbox(processDefinition)', function (obj) {
            console.log(obj)
        });
        //监听工具条
        table.on('tool(processDefinition)', function (obj) {
            var data = obj.data;
            console.log(data);
            if (obj.event === 'detail') {
                detail('查看流程图', 'processDefinitionImg?deploymentId=' + data.deploymentId + '&diagramResourceName=' + data.diagramResourceName, 820, 500);
            } else if (obj.event === 'del') {
                layer.confirm('确定删除流程[<label style="color: #00AA91;">' + data.name + '</label>]?', {
                    btn: ['删除']
                }, function (index) {
                    del(data.deploymentId, false, index);
                });
            }
        });

        $('.layui-col-md12 .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
        $('.select .layui-btn').on('click', function () {
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

    });

    function del(deploymentId, flag, index) {
        $.ajax({
            url: "processDefinitionDel",
            type: "post",
            data: {deploymentId: deploymentId, flag: flag}, async: false,
            success: function (data) {
                var _data = $.parseJSON(data);
                if (_data.code==200) {
                    window.top.layer.msg(_data.msg, {icon: 6, offset: 'rb', area: ['120px', '80px'], anim: 2});
                    layui.table.reload('processDefinitionList');
                } else {
                    window.top.layer.msg(_data.msg, {icon: 5, offset: 'rb', area: ['120px', '80px'], anim: 2});
                }
                layer.close(index);
            }, error: function () {
                alert('error');
                layer.close(index);
            }
        });
    }

    function detail(title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        if (url == null || url == '') {
            url = "error/404";
        }
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        layer.open({
            id: 'process-img',
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false,
            maxmin: true,
            shadeClose: true,
            shade: 0.4,
            title: title,
            content: url,
            //btn:['关闭']
        });
    }

    /*弹出层*/
    /*
     参数解释：
     title   标题
     url     请求的url
     id      需要操作的数据id
     w       弹出层宽度（缺省调默认值）
     h       弹出层高度（缺省调默认值）
     */
    function add(title, url, w, h) {
        if (title == null || title == '') {
            title = false;
        }
        if (url == null || url == '') {
            url = "404.html";
        }
        if (w == null || w == '') {
            w = ($(window).width() * 0.9);
        }
        if (h == null || h == '') {
            h = ($(window).height() - 50);
        }
        layer.open({
            id: 'process-add',
            type: 2,
            area: [w + 'px', h + 'px'],
            fix: false,
            maxmin: true,
            shadeClose: false,
            shade: 0.4,
            title: title,
            content: url
        });
    }
</script>
</body>

</html>
