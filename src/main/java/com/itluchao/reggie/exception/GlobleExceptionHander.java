package com.itluchao.reggie.exception;
//全局异常处理器

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.itluchao.reggie.exception.CustomException;
import com.itluchao.reggie.entity.R;

@ControllerAdvice(annotations = {RestController.class,Controller.class})//拦截上面加了RestController,Controller的 
@ResponseBody//将结果封装成json返回
public class GlobleExceptionHander {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//处理哪些异常
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex)
    {
         return R.error("已存在");
    } 



     @ExceptionHandler(CustomException.class)//处理哪些异常
    public R<String> exceptionHandler(CustomException ex)
    {
         return R.error(ex.getMessage());
    } 
}
