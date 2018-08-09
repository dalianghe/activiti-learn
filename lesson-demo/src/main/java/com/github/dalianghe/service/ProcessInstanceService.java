package com.github.dalianghe.service;

import com.github.dalianghe.model.ProcessHistoryCustom;
import com.github.dalianghe.model.ProcessInstanceCustom;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProcessInstanceService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    /**
     *  查询流程实例
     * @param key
     * @param name
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<ProcessInstanceCustom> getProcessInstances(String key, String name, Integer firstResult, Integer maxResults) {
        List<ProcessInstanceCustom> list = new ArrayList<>();
        ProcessInstanceQuery processInstanceQuery = this.getProcessInstanceQuery(key, name);
        List<ProcessInstance> processInstances = processInstanceQuery
                .listPage((firstResult - 1) * maxResults, maxResults);
        processInstances.stream().forEach(processInstance -> {
            ProcessInstanceCustom processInstanceCustom = new ProcessInstanceCustom();
            BeanUtils.copyProperties(processInstance, processInstanceCustom);
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
            processInstanceCustom.setName(task.getName());
            list.add(processInstanceCustom);
        });
        return list;
    }

    /**
     *  根据查询条件返回ProcessInstanceQuery
     * @param key
     * @param name
     * @return
     */
    private ProcessInstanceQuery getProcessInstanceQuery(String key, String name) {
        ProcessInstanceQuery processInstanceQuery = null;
        if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(name)) {
            processInstanceQuery = runtimeService.createProcessInstanceQuery()
                    .processDefinitionKey(key)
                    .processDefinitionName(name);
        } else if (!StringUtils.isEmpty(key)) {
            processInstanceQuery = runtimeService.createProcessInstanceQuery()
                    .processDefinitionKey(key);
        } else if (!StringUtils.isEmpty(name)) {
            processInstanceQuery = runtimeService.createProcessInstanceQuery()
                    .processDefinitionName(name);
        } else {
            processInstanceQuery = runtimeService.createProcessInstanceQuery();
        }
        return processInstanceQuery;
    }

    /**
     *  查询流程实例总数
     * @param key
     * @param name
     * @return
     */
    public long getProcessInstancesCount(String key, String name) {
        ProcessInstanceQuery processInstanceQuery = this.getProcessInstanceQuery(key, name);
        return processInstanceQuery.count();
    }

    /**
     *  查询流程记录
     * @param processInstanceId
     * @return
     */
    public List<ProcessHistoryCustom> getProcessHistory(String processInstanceId) {
        List<ProcessHistoryCustom> list = new ArrayList<>();
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();
        historicTaskInstances.stream().forEach(historicTaskInstance -> {
            ProcessHistoryCustom processHistoryCustom = new ProcessHistoryCustom();
            processHistoryCustom.setName(historicTaskInstance.getName());
            processHistoryCustom.setAssignee(historicTaskInstance.getAssignee());
            processHistoryCustom.setStartTime(historicTaskInstance.getStartTime());
            processHistoryCustom.setEndTime(historicTaskInstance.getEndTime());
            if(null!=historicTaskInstance.getDurationInMillis()){
                processHistoryCustom.setDuration(historicTaskInstance.getDurationInMillis()/1000+"");
            }
            list.add(processHistoryCustom);
        });
        return list;
    }

    /**
     *  返回流程跟踪图
     * @param processInstanceId
     * @return
     */
    public InputStream getDiagram(String processInstanceId) {
        // 查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        // 获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        // 定义使用宋体
        String fontName = "宋体";
        // 获取流程实例当前节点，需要高亮显示
        List<String> activityIds = new ArrayList<>();
        if(null != processInstance){
            activityIds = runtimeService.getActiveActivityIds(processInstance.getId());
        }else{
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId).list();
            for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
                activityIds.add(historicActivityInstance.getActivityId());
            }
        }
        // 流程走过的节点标红
        /*List<String> activityIds = new ArrayList<>();
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list();
        for(HistoricActivityInstance historicActivityInstance : historicActivityInstances){
            activityIds.add(historicActivityInstance.getActivityId());
        }*/
        List<String> highLightedFlows = Arrays.asList("");
        ProcessDiagramGenerator imageGenerator = new DefaultProcessDiagramGenerator();
        InputStream diagram = imageGenerator.generateDiagram(model, activityIds, highLightedFlows, fontName, fontName, fontName);
        return diagram;
    }

    public void delProcessInstance(String processInstanceId){
        runtimeService.deleteProcessInstance(processInstanceId, "");
    }

}
