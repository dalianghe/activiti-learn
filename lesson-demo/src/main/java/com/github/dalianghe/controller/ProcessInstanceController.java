package com.github.dalianghe.controller;

import com.github.dalianghe.common.OutputJson;
import com.github.dalianghe.common.ReturnFormat;
import com.github.dalianghe.model.ProcessHistoryCustom;
import com.github.dalianghe.model.ProcessInstanceCustom;
import com.github.dalianghe.service.ProcessInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/activiti")
public class ProcessInstanceController {

    Logger logger = LoggerFactory.getLogger(ProcessInstanceController.class);

    @Autowired
    private ProcessInstanceService processInstanceService;

    @RequestMapping("/processInstanceList")
    public String processInstanceList(){
        return "activiti/processInstanceList";
    }

    @RequestMapping("/processInstanceListData")
    @ResponseBody
    public OutputJson processInstanceListData(String key, String name, Integer page, Integer limit){
        List<ProcessInstanceCustom> list = processInstanceService.getProcessInstances(key, name, page, limit);
        return ReturnFormat.retParam(0, processInstanceService.getProcessInstancesCount(key, name), list);
    }

    @RequestMapping("/processHistoryList")
    public String processHistoryList(String processInstanceId, ModelMap map){
        map.put("processInstanceId", processInstanceId);
        return "activiti/processHistoryList";
    }

    @RequestMapping("/processHistoryListData")
    @ResponseBody
    public OutputJson processHistoryListData(String processInstanceId){
        List<ProcessHistoryCustom> list = processInstanceService.getProcessHistory(processInstanceId);
        return ReturnFormat.retParam(0, 0L, list);
    }

    @RequestMapping("/processInstanceImg")
    public void showDiagram(String processInstanceId, HttpServletResponse response) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = processInstanceService.getDiagram(processInstanceId);
            BufferedInputStream reader = new BufferedInputStream(in);
            BufferedOutputStream writer = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = new byte[1024 * 1024];
            int length = reader.read(bytes);
            while ((length > 0)) {
                writer.write(bytes, 0, length);
                length = reader.read(bytes);
            }
            reader.close();
            writer.close();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null!=in) in.close();
                if(null!=out) out.close();
            } catch (Exception e) {
            }
        }
    }

    @RequestMapping("/processInstanceDel")
    @ResponseBody
    public OutputJson processInstanceDel(String processInstanceId){
        processInstanceService.delProcessInstance(processInstanceId);
        return ReturnFormat.retParam(200, null, null);
    }

}
