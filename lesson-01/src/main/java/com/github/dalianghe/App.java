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
 * Hello world!
 */
public class App {

    static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 部署流程
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("bpmn/FinancialReportProcess.bpmn20.xml").deploy();
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        for(Deployment deployment : deployments){
            log.info("部署流程ID： "+deployment.getId());
        }
        // 启动待办
        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport", UUID.randomUUID().toString());
        log.info("流程实例ID： "+processInstance.getId() /*+ " ，业务ID："+processInstance.getBusinessKey()*/);

        // 查询填写待办
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        for(Task task : tasks){
            log.info("任务名称：" + task.getName());
        }

        Map<String,Object> variables = new HashMap<String,Object>();
        variables.put("userId", "hdl");
        // 处理任务
        taskService.complete(tasks.get(0).getId(), variables);

        // 查询我的办理
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().taskAssignee("hdl").list();
        for(HistoricTaskInstance historicTaskInstance : historicTaskInstances){
            log.info("我的办理：" + historicTaskInstance.getId());
        }

        // 查询审核待办
        List<Task> tasks2 = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for(Task task : tasks2){
            log.info("任务名称：" + task.getName());
        }
        // 处理任务
        taskService.complete(tasks2.get(0).getId());

    }

    /*public static void history(){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().list();
        for(HistoricProcessInstance historicProcessInstance : historicProcessInstances){
            log.info(historicProcessInstance.getId());
        }

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().list();
        for(HistoricTaskInstance historicTaskInstance : historicTaskInstances){
            log.info(historicTaskInstance.getId());
        }
    }*/

}
