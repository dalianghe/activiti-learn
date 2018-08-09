# ACTIVITI之部署流程资源文件

Acticiti可通过编程方式部署流程资源文件yi以及配置druid数据源，可部署三种类型的文件：

1、bpmn后缀文件

2、bpmn20.xml后缀文件

3、zip压缩包后缀文件

## 部署bpmn流程文件

    // 指定配置文件及bean名称来创建ProcessEngineConfiguration对象
    ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
    // 构建ProcessEngine对象
    ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    // 创建RepositoryService对象部署流程文件
    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment = repositoryService.createDeployment()
            .name("休假流程") // 设置流程部署的名称，对应act_re_deployment表的name_字段
            .addClasspathResource("bpmn/Vacation.bpmn")
            .deploy();
    logger.info("部署主键：" + deployment.getId());

## 部署xml流程文件

    // 使用指定配置文件名称方式创建ProcessEngineConfiguration
    ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
            .createProcessEngineConfigurationFromResource("activiti.cfg.xml");
    // 构建ProcessEngine工作流引擎对象
    ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    // 创建RepositoryService对象部署流程文件
    RepositoryService repositoryService = processEngine.getRepositoryService();
    Deployment deployment = repositoryService.createDeployment()
            .name("休假流程") // 设置流程部署的名称，对应act_re_deployment表的name_字段
            .addClasspathResource("bpmn/Vacation.bpmn20.xml")
            .deploy();
    logger.info("部署主键：" + deployment.getId());

## 部署zip包

    // 根据默认方式实例化ProcessEngineConfiguration
    ProcessEngineConfiguration processEngineConfiguration =
            ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
    // 构造ProcessEngine实例
    ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
    RepositoryService repositoryService = processEngine.getRepositoryService();
    // 部署资源包（必须为zip包）
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("bpmn/休假流程.zip");
    ZipInputStream zipInputStream = new ZipInputStream(inputStream);
    Deployment deployment3 = repositoryService.createDeployment()
            .name("休假流程") // 设置流程部署的名称，对应act_re_deployment表的name_字段
            // 添加zip资源文件包
            .addZipInputStream(zipInputStream)
            .deploy();
    log.info("部署主键：" + deployment3.getId());
    
说明：Activiti7版本在部署bpmn或xml流程文件时，不会部署流程图。
    
## 配置druid数据源

* 添加依赖

添加druid数据源依赖包：

    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>1.0.26</version>
    </dependency>
    
* 修改配置文件

增加datasource bean的配置，代码如下：

    <!-- 配置数据源 -->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
        <!-- 基本属性 url、user、password -->
        <property name="url" value="jdbc:mysql://localhost:3306/activititest" />
        <property name="username" value="root" />
        <property name="password" value="root" />
        <!-- 配置初始化大小、最小、最大 -->
        <property name="initialSize" value="1" />
        <property name="minIdle" value="1" />
        <property name="maxActive" value="20" />
        <!-- 配置获取连接等待超时的时间 -->
        <property name="maxWait" value="60000" />
    </bean>
    <!-- 配置ProcessEngineConfiguration -->
    <bean id="processEngineConfiguration" class="org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration">
        <!-- 引入dataSource -->
        <property name="dataSource" ref="dataSource" />
        <property name="databaseSchemaUpdate" value="true"/>
    </bean>