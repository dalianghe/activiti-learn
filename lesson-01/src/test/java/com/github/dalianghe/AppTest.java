package com.github.dalianghe;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void shouldAnswerWithTrue() {

        ProcessEngine processEngine = activitiRule.getProcessEngine();
        System.out.println(processEngine);

    }
}
