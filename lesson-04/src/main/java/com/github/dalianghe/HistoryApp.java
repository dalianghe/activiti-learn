package com.github.dalianghe;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HistoryApp {

    static Logger logger = LoggerFactory.getLogger(HistoryApp.class);

    public static void main(String[] args) {

        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();

        // 对应表：act_hi_actinst
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().list();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            logger.info(historicActivityInstance.getId());
        }

        // 获取历史流程实例记录 对应表：act_hi_procinst
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().list();
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            logger.info(historicProcessInstance.getId());
        }

        // 获取历史任务实例记录 对应表：act_hi_taskinst
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("dalianghe")
                .finished()
                .list();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            logger.info("-->" + historicTaskInstance.getProcessInstanceId());
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
            logger.info("---->" + historicProcessInstance.getBusinessKey());
        }

        /*ProcessInstanceHistoryLogQuery processInstanceHistoryLogQuery = historyService.createProcessInstanceHistoryLogQuery("7501");
        ProcessInstanceHistoryLog processInstanceHistoryLog = processInstanceHistoryLogQuery.singleResult();
        logger.info(processInstanceHistoryLog.getId());*/

    }

}
