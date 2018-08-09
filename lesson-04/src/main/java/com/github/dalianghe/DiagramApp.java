package com.github.dalianghe;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;

import java.io.*;
import java.util.List;

public class DiagramApp {

    public static void main(String[] args) throws IOException {

        ProcessEngineConfiguration processEngineConfiguration =
                ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        ProcessDiagramGenerator imageGenerator = new DefaultProcessDiagramGenerator();
        String activityFontName = imageGenerator.getDefaultActivityFontName();
        String labelFontName = imageGenerator.getDefaultLabelFontName();
        String annotationFontName = imageGenerator.getDefaultAnnotationFontName();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        List<String> activityIds = runtimeService.getActiveActivityIds("55001");
        BpmnModel bpmnModel = repositoryService.getBpmnModel("Vacation:1:52504");

        InputStream diagram = imageGenerator.generateDiagram(bpmnModel, activityIds);

        byte[] buffer = new byte[diagram.available()];
        diagram.read(buffer);

        File targetFile = new File("d:/aaa.jpg");
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
    }

}
