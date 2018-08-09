package com.github.dalianghe.service;

import com.github.dalianghe.entity.VacationEntity;
import com.github.dalianghe.mapper.VacationMapper;
import com.github.dalianghe.model.VacationHistoryCustom;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VacationService {

    @Autowired
    private VacationMapper vacationMapper;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    public List<VacationHistoryCustom> getHistory(String userId, Integer firstResult, Integer maxResults){

        List<VacationHistoryCustom> list = new ArrayList<>();

        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .listPage((firstResult - 1) * maxResults, maxResults);

        historicTaskInstances.stream().forEach(historicTaskInstance -> {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
            VacationHistoryCustom vacationHistoryCustom = new VacationHistoryCustom();
            vacationHistoryCustom.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            vacationHistoryCustom.setProcessDefinitionVersion(historicProcessInstance.getProcessDefinitionVersion());
            vacationHistoryCustom.setProcessInstanceId(historicTaskInstance.getProcessInstanceId());
            vacationHistoryCustom.setBusinessKey(historicProcessInstance.getBusinessKey());
            vacationHistoryCustom.setName(historicTaskInstance.getName());
            vacationHistoryCustom.setAssignee(historicTaskInstance.getAssignee());
            vacationHistoryCustom.setStartTime(historicTaskInstance.getStartTime());
            vacationHistoryCustom.setEndTime(historicTaskInstance.getEndTime());
            list.add(vacationHistoryCustom);
        });
        return list;
    }

    public long getHistoryCount(String userId){
        return historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .count();
    }

    @Transactional
    public VacationEntity saveEntity(VacationEntity vacationEntity){
        vacationMapper.insertEntity(vacationEntity);
        return vacationEntity;
    }

    @Transactional
    public void startVacationProcess(VacationEntity vacationEntity){

        // step1:保存业务数据
        vacationEntity = this.saveEntity(vacationEntity);
        // step2:启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Vacation",vacationEntity.getId()+"");
        // 初始化任务参数
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("arg", vacationEntity);
        // 查询第一个任务
        Task firstTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        // 设置任务受理人
        taskService.setAssignee(firstTask.getId(), "dalianghe");
        // 完成任务
        taskService.complete(firstTask.getId(), vars);

    }

}
