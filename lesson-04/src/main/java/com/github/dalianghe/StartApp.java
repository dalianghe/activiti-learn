package com.github.dalianghe;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Hello world!
 */
public class StartApp {

    private static Logger logger = LoggerFactory.getLogger(StartApp.class);

    public static void main(String[] args) {

        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 启动流程
        /*Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyUserId", "dalianghe");
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("VacationNew", "123", variables );
        logger.info("流程实例ID：" + processInstance.getId());*/

        // 办理申请
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId("2501").singleResult();
        //taskService.claim(task.getId(), "dalianghe");
        taskService.complete(task.getId());
    }


}
