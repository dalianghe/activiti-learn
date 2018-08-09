package com.github.dalianghe;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple DeployApp.
 */
public class AppTest {

    Logger log = LoggerFactory.getLogger(AppTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"bpmn/VacationRequest.bpmn20.xml"})
    public void shouldAnswerWithTrue() {

        //ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("vacationRequest");
        //System.out.println(processInstance);

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employeeName", "何大亮");
        variables.put("numberOfDays", new Integer(4));
        variables.put("vacationMotivation", "我累了。。。");

        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);

        log.info("Number of process instances: " + runtimeService.createProcessInstanceQuery().count());

        TaskService taskService = activitiRule.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        for(Task task : tasks){
            log.info("TaskQ available: " + task.getName());
        }
        Task task = tasks.get(0);
        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("vacationApproved", "false");
        taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);
    }
}
