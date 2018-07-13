package com.github.dalianghe;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BpmnApp {

    static Logger logger = LoggerFactory.getLogger(BpmnApp.class);

    public static void main(String[] args) {

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
                // 可添加多个资源文件
                //.addClasspathResource("bpmn/Vacation.png")
                .deploy();
        logger.info("部署主键：" + deployment.getId());

    }

}
