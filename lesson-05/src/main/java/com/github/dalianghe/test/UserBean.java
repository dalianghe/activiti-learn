package com.github.dalianghe.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBean {

    private RepositoryService repositoryService;

    private RuntimeService runtimeService;

    @Autowired
    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }
    @Autowired
    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Transactional
    public void hello() throws Exception{
        // here you can do transactional stuff in your domain model
        // and it will be combined in the same transaction as
        // the startProcessInstanceByKey to the Activiti RuntimeService
        //ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloProcess");
        //System.out.println(processInstance);

        String deploymentId = repositoryService
                .createDeployment()
                .addClasspathResource("bpmn/hello.bpmn20.xml")
                .deploy()
                .getId();
        System.out.println("deploymentId: " + deploymentId);
        //start();
    }

    @Transactional
    public void start() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloProcess");
        System.out.println(processInstance);
        throw new RuntimeException("测试事物");
    }

}
