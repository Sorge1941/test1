package com.itluchao.reggie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.dto.DishDto;
import com.itluchao.reggie.entity.Dish;
import com.itluchao.reggie.entity.DishFlavor;
import com.itluchao.reggie.mapper.DishMapper;
import com.itluchao.reggie.service.DishFlavorService;
import com.itluchao.reggie.service.DishService;

@Service
public class DishServiceimpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorService dishFlavorService;

    // 要往两个表里面加数据
    @Override
    public void AddDish(DishDto dishDto) {

        this.save(dishDto);

        List<DishFlavor> flavors = dishDto.getFlavors();
        // 每个菜品包括多个flavor,每次只能添加一个菜品，所以dishDto.getId()只有一个
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //批量添加
        dishFlavorService.saveBatch(flavors);
    }


    //修改菜品
    @Override
    public void updateDish(DishDto dishDto) {
        QueryWrapper<Dish> queryWrapper=new QueryWrapper<Dish>();
        queryWrapper.eq("id",dishDto.getId());
        //根据ID更新Dish表
        this.update(dishDto,queryWrapper);

        //更新dish——flavors表格
        //查询原来的表格
        //条件
        QueryWrapper<DishFlavor> queryWrapper2=new QueryWrapper<DishFlavor>();
        queryWrapper2.eq("dish_id",dishDto.getId());
        //把原来的先删掉
        dishFlavorService.remove(queryWrapper2);

        //重新加回来
        List<DishFlavor> flavors=dishDto.getFlavors();
        //为list设置id
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        
        dishFlavorService.saveBatch(flavors);
    }

    //停售
    @Override
    public void stop(Integer status,  long ids) {
        UpdateWrapper<Dish> queryWrapper=new UpdateWrapper<Dish>();
       queryWrapper.eq("id",ids);
        Dish dish=new Dish();
        dish.setStatus(status);
        this.update(dish,queryWrapper);
    }

    //删除菜品
    @Override
    public void DeleteDish(long ids) {
        QueryWrapper<Dish> queryWrapper=new QueryWrapper<Dish>();
        queryWrapper.eq("id",ids);
        this.remove(queryWrapper);

        //删除dishfalover的
        QueryWrapper<DishFlavor> queryWrapper2=new QueryWrapper<DishFlavor>();
        queryWrapper2.eq("dish_id",ids);
        dishFlavorService.remove(queryWrapper2);

    }

}
