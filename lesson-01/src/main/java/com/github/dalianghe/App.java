package com.github.dalianghe;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Activiti Hello world!
 */
public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        // 获得ProcessEngine流程引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // RepositoryService对象提供流程定义的存储和部署服务
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 部署流程定义，即我们编写的bpmn2.0流程文件
        repositoryService.createDeployment().addClasspathResource("bpmn/FinancialReportProcess.bpmn20.xml").deploy();
        // 查询已部署的流程定义
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        for(Deployment deployment : deployments){
            logger.info("部署流程ID： "+deployment.getId());
        }

        // RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 启动流程，流程流转至下一节点，本例即"填写财务报表"
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport");
        logger.info("流程实例ID： "+processInstance.getId());

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

        // 查询审核待办任务,根据组条件（management）查询
        List<Task> tasks2 = taskService.createTaskQuery().taskCandidateGroup("management").list();
        tasks2.stream().forEach(task -> {
            logger.info("任务名称：" + task.getName());
            taskService.claim(task.getId(), "wangwu");
            // 处理任务，流程流转至下一节点，本例即结束节点
            taskService.complete(task.getId());
        });

    }

}
