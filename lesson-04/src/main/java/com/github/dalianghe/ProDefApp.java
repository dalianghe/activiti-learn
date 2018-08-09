package com.github.dalianghe;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProDefApp {

    static Logger log = LoggerFactory.getLogger(ProDefApp.class);

    public static void main(String[] args) {

        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();

        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 查询流程部署信息，表act_re_deployment
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> deployments = deploymentQuery.list();
        for(Deployment deployment1 : deployments){
            log.info("流程部署ID：" + deployment1.getId());
        }

        // 查询流程定义信息，表act_re_prodef
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        long count = processDefinitionQuery.count();
        log.info("流程定义总数条数：" + count);

        // 返回流程定义信息的集合列表
        List<ProcessDefinition> processDefinitions =
                processDefinitionQuery
                        .orderByProcessDefinitionKey().asc()
                        .orderByProcessDefinitionVersion().desc()
                        .list();
                        //.listPage(1,1); // 分页查询
        for(ProcessDefinition processDefinition : processDefinitions){
            log.info("排序查询流程定义ID：" + processDefinition.getId());
        }

        // 模糊查询流程定义
        List<ProcessDefinition> list =
                processDefinitionQuery
                        .processDefinitionNameLike("请假%")
                        .list();
        for(ProcessDefinition pd : list){
            log.info("模糊查询流程定义ID：" + pd.getId());
        }

        // 精确查询流程定义
        ProcessDefinition processDefinition =
                processDefinitionQuery
                        .processDefinitionId("ProDefApp:2:2504") // 按流程定义id
                        //.deploymentId("1") // 按流程部署id
                        .singleResult(); // 返回单条
        log.info("精确查询流程定义ID：" + processDefinition.getId());

        // 删除流程部署，cascade=true 级联删除流程实例、历史记录和jobs
        //repositoryService.deleteDeployment("1", true);

        // 关闭引擎
        // processEngine.close();
    }

}
