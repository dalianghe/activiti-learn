package com.github.dalianghe.controller;

import com.github.dalianghe.common.OutputJson;
import com.github.dalianghe.common.ReturnFormat;
import com.github.dalianghe.entity.VacationEntity;
import com.github.dalianghe.model.VacationCustom;
import com.github.dalianghe.model.VacationHistoryCustom;
import com.github.dalianghe.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/vacation")
public class VacationController {

    @Autowired
    private VacationService vacationService;

    @RequestMapping("/applyList")
    public String applyList(){
        return "vacation/applyList";
    }

    @RequestMapping("/applyListData")
    @ResponseBody
    public OutputJson applyListData(Integer page, Integer limit){
        List<VacationHistoryCustom> list = vacationService.getHistory("dalianghe", page, limit);
        return ReturnFormat.retParam(0, vacationService.getHistoryCount("dalianghe"), list);
    }

    @RequestMapping("/applyAdd")
    public String applyAdd(){
        return "vacation/applyAdd";
    }

    @RequestMapping(value = "/vacationApply", method = RequestMethod.POST)
    @ResponseBody
    public OutputJson vacationApply(VacationCustom vacationCustom) throws ParseException {
        VacationEntity vacationEntity = new VacationEntity();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        vacationEntity.setStartDate(sdf.parse(vacationCustom.getStartDate()));
        vacationEntity.setEndDate(sdf.parse(vacationCustom.getEndDate()));
        vacationEntity.setDays(vacationCustom.getDays());
        vacationEntity.setVacationType(vacationCustom.getVacationType());
        vacationEntity.setReason(vacationCustom.getReason());
        vacationService.startVacationProcess(vacationEntity);
        return ReturnFormat.retParam(200, null, null);
    }

}
