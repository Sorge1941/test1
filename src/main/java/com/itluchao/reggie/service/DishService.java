package com.itluchao.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itluchao.reggie.dto.DishDto;
import com.itluchao.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
//添加菜品
 public   void AddDish(DishDto dishDto);

 //修改菜品
public void updateDish(DishDto dishDto);

//停售
public void stop(Integer status, long ids);
//删除菜品
public void DeleteDish(long ids);

}
