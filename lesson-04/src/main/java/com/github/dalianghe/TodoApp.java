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

/**
 * Hello world!
 */
public class TodoApp {

    private static Logger logger = LoggerFactory.getLogger(TodoApp.class);

    public static void main(String[] args) {

        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateGroup("deptLeader");
        List<Task> tasks = taskQuery.list();
        tasks.stream().forEach(task -> {
            taskService.claim(task.getId(), "leader");
            Map<String,Object> map = new HashMap<>();
            map.put("deptLeaderPass", true);
            taskService.complete(task.getId(), map);
        });
    }


}
