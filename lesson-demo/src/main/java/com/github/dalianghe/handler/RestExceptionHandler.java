package com.github.dalianghe.handler;

import java.io.IOException;
import java.text.ParseException;

import com.github.dalianghe.common.OutputJson;
import com.github.dalianghe.common.ReturnFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    //运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public OutputJson runtimeExceptionHandler(RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return ReturnFormat.retParam(1000, null, null);
    }

    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public OutputJson nullPointerExceptionHandler(NullPointerException ex) {
        ex.printStackTrace();
        return ReturnFormat.retParam(1001, null, null);
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    @ResponseBody
    public OutputJson classCastExceptionHandler(ClassCastException ex) {
        ex.printStackTrace();
        return ReturnFormat.retParam(1002, null, null);
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public OutputJson iOExceptionHandler(IOException ex) {
        ex.printStackTrace();
        return ReturnFormat.retParam(1003, null, null);
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    @ResponseBody
    public OutputJson noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        ex.printStackTrace();
        return ReturnFormat.retParam(1004, null, null);
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseBody
    public OutputJson indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        ex.printStackTrace();
        return ReturnFormat.retParam(1005, null, null);
    }

    //数组越界异常
    @ExceptionHandler(ParseException.class)
    @ResponseBody
    public OutputJson parseExceptionHandler(ParseException ex) {
        ex.printStackTrace();
        return ReturnFormat.retParam(1006, null, null);
    }

    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public OutputJson requestNotReadable(HttpMessageNotReadableException ex) {
        logger.info("400..requestNotReadable");
        ex.printStackTrace();
        return ReturnFormat.retParam(400, null, null);
    }

    //400错误
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public OutputJson requestTypeMismatch(TypeMismatchException ex) {
        logger.info("400..TypeMismatchException");
        ex.printStackTrace();
        return ReturnFormat.retParam(400, null, null);
    }

    //400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public OutputJson requestMissingServletRequest(MissingServletRequestParameterException ex) {
        System.out.println("400..MissingServletRequest");
        ex.printStackTrace();
        return ReturnFormat.retParam(400, null, null);
    }

    //405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public OutputJson request405() {
        logger.info("405...");
        return ReturnFormat.retParam(405, null, null);
    }

    //406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public OutputJson request406() {
        logger.info("404...");
        return ReturnFormat.retParam(406, null, null);
    }

    //500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    @ResponseBody
    public OutputJson server500(RuntimeException runtimeException) {
        logger.info("500...");
        return ReturnFormat.retParam(406, null, null);
    }

}
