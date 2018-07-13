package com.github.dalianghe;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * Hello world!
 */
public class ZipApp {

    static Logger log = LoggerFactory.getLogger(ZipApp.class);

    public static void main(String[] args) {

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

    }

}
