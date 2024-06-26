package com.itluchao.reggie.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itluchao.reggie.entity.Dish;
//菜品
@Mapper
public interface DishMapper extends BaseMapper<Dish>{

}
