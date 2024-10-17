 package com.itluchao.reggie.exception;
 import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
 import org.springframework.stereotype.Component;
 import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
 @Component
 public class MetaObject implements MetaObjectHandler{
     //插入时的方法
     @Override
     public void insertFill( org.apache.ibatis.reflection.MetaObject metaObject) {
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
     }
     //更新时的方法
     @Override
     public void updateFill(org.apache.ibatis.reflection.MetaObject metaObject) {
         // TODO Auto-generated method stub
            metaObject.setValue("updateTime",LocalDateTime.now());
            metaObject.setValue("updateUser",BaseContext.getCurrentId());
     }
 }
