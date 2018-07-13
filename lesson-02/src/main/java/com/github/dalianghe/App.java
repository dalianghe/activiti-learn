package com.github.dalianghe;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 加载配置文件方式演示
 */
public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception{

        /*******************读取Activiti配置文件方式一：使用ProcessEngineConfiguration抽象类******************/
        // 一、使用默认配置方式创建ProcessEngineConfiguration实例
        // 默认方式指Activiti引擎读取classpath下的、文件名为activiti.cfg.xml的配置文件，并且配置文件中bean id为processEngineConfiguration的bean
        ProcessEngineConfiguration defaultProcessEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        // 构造ProcessEngine实例时，进行数据库表结构的创建
        ProcessEngine defaultProcessEngine = defaultProcessEngineConfiguration.buildProcessEngine();
        logger.info("使用默认配置文件构造的流程实例：" + defaultProcessEngine);

        // 二、使用自定义配置文件创建ProcessEngineConfiguration实例
        // 1）指定配置文件路径及名称
        ProcessEngineConfiguration resource1ProcessEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg-resource1.xml");
        // 设置流程引擎名称，默认为：default
        resource1ProcessEngineConfiguration.setProcessEngineName("customProcessEngineName1");
        ProcessEngine resource1ProcessEngine = resource1ProcessEngineConfiguration.buildProcessEngine();
        logger.info("使用自定义配置文件构造的流程实例：" + resource1ProcessEngine);
        // 2) 指定配置文件路径、名称及配置文件中bean id
        ProcessEngineConfiguration resource2ProcessEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti-cfg-resource2.xml", "myProcessEngineConfiguration");
        resource2ProcessEngineConfiguration.setProcessEngineName("customProcessEngineName2");
        ProcessEngine resource2ProcessEngine = resource2ProcessEngineConfiguration.buildProcessEngine();
        logger.info("使用自定义配置文件及自定义bean名称构造的流程实例：" + resource2ProcessEngine);

        // 三、使用配置文件输入流创建ProcessEngineConfiguration实例
        //1） 指定输入流
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream1 = classLoader.getResourceAsStream("activiti-cfg-inputStream1.xml");
        ProcessEngineConfiguration inputStream1ProcessEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(inputStream1);
        ProcessEngine inputStream1ProcessEngine = inputStream1ProcessEngineConfiguration.buildProcessEngine();
        logger.info("使用配置文件输入流程构造的流程实例：" + inputStream1ProcessEngine);
        //2） 指定输入流及Bean Id
        ClassLoader classLoader2 = Thread.currentThread().getContextClassLoader();
        InputStream inputStream2 = classLoader2.getResourceAsStream("activiti-cfg-inputStream2.xml");
        ProcessEngineConfiguration inputStream2ProcessEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(inputStream2, "myProcessEngineConfiguration");
        ProcessEngine inputStream2ProcessEngine = inputStream2ProcessEngineConfiguration.buildProcessEngine();
        logger.info("使用配置文件输入流及bean id程构造的流程实例：" + inputStream2ProcessEngine);


        /*******************读取Activiti配置文件方式二：使用ProcessEngines帮助类******************/
        //ProcessEngine processEngine1 = ProcessEngines.getDefaultProcessEngine();
        //ProcessEngine processEngine2 = ProcessEngines.getProcessEngine("custom");
        Map<String, ProcessEngine> engines = ProcessEngines.getProcessEngines();
        logger.info("大小：" + engines.size());

    }
}
