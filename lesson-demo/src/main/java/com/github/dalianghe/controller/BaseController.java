package com.github.dalianghe.controller;

public class BaseController {

    protected String retContent(int status,Object data) {
        return "";//ReturnFormat.retParam(status, data);
    }

}
