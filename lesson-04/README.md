# ACTIVITI之启动流程

在流程文件部署成功后，即可以启动工作流。

## 启动工作流

启动工作流需要使用Activiti提供的RuntimeService类进行，如下：

    // 启动流程
    RuntimeService runtimeService = processEngine.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Vacation");
                     
启动工作流方法有几个重要的重载，分别为：

    /**
     * processDefinitionKey参数为流程定义的id，即流程文件中process标签的id
     */
    ProcessInstance startProcessInstanceByKey(String processDefinitionKey);
    
    /**
     * processDefinitionKey参数为流程定义的id，即流程文件中process标签的id
     * businessKey参数为业务id，主要是跟业务数据关联
     */
    ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey);
    
    /**
     * processDefinitionKey参数为流程定义的id，即流程文件中process标签的id
     * businessKey参数为业务id，主要是跟业务数据关联
     * variables参数为流程文件中使用的参数，可通过key-value方式传入
     */
    ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables);                     
                         
          