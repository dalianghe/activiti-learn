# Activiti之初体验

本例是Activiti之初体验，完成财务月度报表提报流程，流程如下：

开始 --> 填写月度财务报告 --> 审核月度财务报告 --> 结束

技术点：

1、Activiti流程引擎

2、h2内存数据库

3、log4j日志框架

## 环境搭建

### 引入依赖

* 工作流引擎

依赖activiti-engine：

    <dependency>
        <groupId>org.activiti</groupId>
        <artifactId>activiti-engine</artifactId>
        <version>7-201802-EA</version>
    </dependency>

* 数据库

依赖h2内存数据库：
 
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>1.4.197</version>
    </dependency>

* 引入log4j依赖

依赖log4j日志：

    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-log4j12</artifactId>
        <version>1.7.25</version>
    </dependency>

### 编写代码

通过main方法模拟流程办理，步骤如下：

* 获取流程引擎对象


        // 获得ProcessEngine流程引擎对象
    
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

* 获取流程部署对象

    
    // RepositoryService对象提供流程定义的存储和部署服务
    
    RepositoryService repositoryService = processEngine.getRepositoryService();
    
* 部署流程


    // 部署流程定义，即我们编写的bpmn2.0流程文件
    
    repositoryService.createDeployment().addClasspathResource("bpmn/FinancialReportProcess.bpmn20.xml").deploy();
    
* 启动任务


    RuntimeService runtimeService = processEngine.getRuntimeService();
    
    // 启动流程，流程流转至下一节点，本例即"填写财务报表"
    
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport");
    
    logger.info("流程实例ID： "+processInstance.getId());    
    
 * 财报填写
 
 
    
    // 查询待办任务，根据组条件（accountancy）查询
    
    TaskService taskService = processEngine.getTaskService();
    
    List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
    
    // 办理任务
    
    tasks.stream().forEach(task -> {
    
    logger.info("任务名称：" + task.getName());
    
    // 签收任务，指定任务id和办理人
    
    taskService.claim(task.getId(), "zhangsan");
    
    // 处理任务，流程流转至下一节点，本例即"审核财务报表"
    
    taskService.complete(task.getId());
    
    });
 
 * 财报审核
 
 
    // 查询审核待办任务,根据组条件（management）查询
    
    List<Task> tasks2 = taskService.createTaskQuery().taskCandidateGroup("management").list();
    
    tasks2.stream().forEach(task -> {
    
    logger.info("任务名称：" + task.getName());

    taskService.claim(task.getId(), "wangwu");

    // 处理任务，流程流转至下一节点，本例即结束节点

    taskService.complete(task.getId());
    
    });
    
至此完成财报审核流程的办理，详细代码参见App类。