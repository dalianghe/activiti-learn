package com.github.dalianghe.controller;

import com.github.dalianghe.common.OutputJson;
import com.github.dalianghe.common.ReturnFormat;
import com.github.dalianghe.model.ProcessDefinitionCustom;
import com.github.dalianghe.service.ProcessDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping("/activiti")
public class ProcessDefinitionController {

    Logger logger = LoggerFactory.getLogger(ProcessDefinitionController.class);

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @RequestMapping("/processDefinitionList")
    public String processDefinitionList(){
        return "activiti/processDefinitionList";
    }

    @RequestMapping("/processDefinitionListData")
    @ResponseBody
    public OutputJson processDefinitionListData(String key, String name, Integer page, Integer limit){
        logger.info("获取流程定义数据，传入参数key： "+ key + "，name：" + name);
        List<ProcessDefinitionCustom> list = processDefinitionService.getProcessDefinitions(key, name, page, limit);
        logger.info("获取流程定义数据，返回list大小：" + list.size());
        return ReturnFormat.retParam(0, processDefinitionService.getProcessDefinitionsCount(key, name), list);
    }

    @RequestMapping("/processDefinitionDel")
    @ResponseBody
    public OutputJson processDefinitionDel(String deploymentId){
        processDefinitionService.delProcessDefinition(deploymentId);
        return ReturnFormat.retParam(200, null, null);
    }

    @RequestMapping("/processDefinitionImg")
    public void viewImage(String deploymentId, String diagramResourceName, HttpServletResponse resp){
        InputStream in = processDefinitionService.getImageStream(deploymentId, diagramResourceName);
        OutputStream out = null;
        try {
            out = resp.getOutputStream();
            // 把图片的输入流程写入resp的输出流中
            byte[] b = new byte[1024];
            for (int len = -1; (len= in.read(b))!=-1; ) {
                out.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭流
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/processDefinitionAdd")
    public String processDefinitionAdd(){
        return "activiti/processDefinitionAdd";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public OutputJson upload(@RequestParam("file") MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        logger.info("流程发布，上传文件名：" + fileName);
        InputStream inputStream = file.getInputStream();
        String deploymentId = processDefinitionService.publishProcessDefinition(fileName, inputStream);
        logger.info("流程发布，返回部署ID：" + deploymentId);
        inputStream.close();
        return ReturnFormat.retParam(200, null, null);
    }

}
