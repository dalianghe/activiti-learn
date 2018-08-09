# Activiti之加载配置文件

本篇介绍ProcessEngineConfiguration类，该类是Activiti构造流程引擎对象（ProcessEngine）的配置类，即通过该类的配置信息构建流程引擎。

ProcessEngineConfiguration是抽象类，该类的子类关系如下：

    ProcessEngineConfiguration
        |->ProcessEngineConfigurationImpl
            |->MultiSchemaMultiTenantProcessEngineConfiguration
            |->SpringProcessEngineConfiguration
            |->JtaProcessEngineConfiguration
            |->StandaloneProcessEngineConfiguration
                |->StandaloneInMemProcessEngineConfiguration

本节主要使用StandaloneProcessEngineConfiguration为例来进行介绍。

技术点：

1、Activiti流程引擎

1、mysql数据库

2、log4j2日志框架

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

依赖mysql驱动：
 
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.38</version>
    </dependency>

* 引入log4j2依赖

依赖log4j日志：

    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.11.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.11.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j-impl</artifactId>
        <version>2.11.0</version>
    </dependency>

### 编写代码

* 默认方式

所谓默认方式即流程引擎默认加载classpath下的、文件名为activiti.cfg.xml的配置文件，并且配置文件中bean id为processEngineConfiguration的beanProcessEngineConfiguration实例，代码如下：

    ProcessEngineConfiguration defaultProcessEngineConfiguration =
                    ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
    ProcessEngine defaultProcessEngine = defaultProcessEngineConfiguration.buildProcessEngine();              

* 指定文件名

即自定义配置文件的路径及名称的方式加载，代码如下：

    ProcessEngineConfiguration resource1ProcessEngineConfiguration =
                    ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg-resource1.xml");
    ProcessEngine resource1ProcessEngine = resource1ProcessEngineConfiguration.buildProcessEngine();
    
* 指定bean名称

即自定义ProcessEngineConfiguration的bean名称，代码如下：

    ProcessEngineConfiguration resource2ProcessEngineConfiguration =
                    ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg-resource2.xml", "myProcessEngineConfiguration");
    ProcessEngine resource2ProcessEngine = resource2ProcessEngineConfiguration.buildProcessEngine();                                       

* 流方式

通过InputStream方式加载，代码如下：

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream1 = classLoader.getResourceAsStream("activiti-cfg-inputStream1.xml");
    ProcessEngineConfiguration inputStream1ProcessEngineConfiguration =
            ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(inputStream1);
    ProcessEngine inputStream1ProcessEngine = inputStream1ProcessEngineConfiguration.buildProcessEngine();

改方式亦可自定义bean的名称。

* 通过ProcessEngines工具类

