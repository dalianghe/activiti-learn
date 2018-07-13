package com.github.dalianghe;


import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for simple ZipApp.
 */
public class AppTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void shouldAnswerWithTrue() {
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        System.out.println("第一个任务完成前，当前任务名称：" + task.getName());
    }
}
