package com.github.dalianghe;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Hello world!
 */
public class DeployApp {

    private static Logger logger = LoggerFactory.getLogger(DeployApp.class);

    public static void main(String[] args) {

        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 流程部署
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .name("休假流程") // 设置流程部署的名称，对应act_re_deployment表的name_字段
                .addClasspathResource("bpmn/VacationNew.bpmn20.xml")
                .deploy();
        logger.info("部署主键：" + deployment.getId());

    }

}
