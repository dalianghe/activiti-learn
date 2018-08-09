package com.github.dalianghe.service;

import com.github.dalianghe.model.ProcessDefinitionCustom;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

@Service
public class ProcessDefinitionService {

    @Autowired
    private RepositoryService repositoryService;

    public long getProcessDefinitionsCount(String key, String name) {
        return this.getProcessDefinitionQuery(key, name).count();
    }

    public List<ProcessDefinitionCustom> getProcessDefinitions(String key, String name, Integer firstResult, Integer maxResults) {
        List<ProcessDefinitionCustom> list = new ArrayList<>();
        ProcessDefinitionQuery processDefinitionQuery = this.getProcessDefinitionQuery(key, name);
        List<ProcessDefinition> processDefinitions = processDefinitionQuery
                .orderByProcessDefinitionKey().asc()
                .orderByProcessDefinitionVersion().desc()
                .listPage((firstResult - 1) * maxResults, maxResults);

        //lambda循环
        processDefinitions.stream().forEach(processDefinition -> {
            ProcessDefinitionCustom processDefinitionCustom = new ProcessDefinitionCustom();
            BeanUtils.copyProperties(processDefinition, processDefinitionCustom);
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(processDefinition.getDeploymentId()).singleResult();
            processDefinitionCustom.setCreateDate(deployment.getDeploymentTime());
            list.add(processDefinitionCustom);
        });
        return list;
    }

    private ProcessDefinitionQuery getProcessDefinitionQuery(String key, String name) {
        ProcessDefinitionQuery processDefinitionQuery = null;
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(name)) {
            processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(key)
                    .processDefinitionNameLike(name + "%");
        } else if (!StringUtils.isEmpty(key)) {
            processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(key);
        } else if (!StringUtils.isEmpty(name)) {
            processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionNameLike(name + "%");
        } else {
            processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        }
        return processDefinitionQuery;
    }

    public InputStream getImageStream(String deploymentId, String resourceName){
        return repositoryService.getResourceAsStream(deploymentId, resourceName);
    }

    public void delProcessDefinition(String deploymentId){
        repositoryService.deleteDeployment(deploymentId, true);
    }

    public String publishProcessDefinition(String processName, InputStream inputStream){
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment()
                .name(processName)
                .addZipInputStream(zipInputStream).deploy();

        return deployment.getId();
    }

}
