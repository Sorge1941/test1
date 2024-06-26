package com.itluchao.reggie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.dto.SetmealDto;
import com.itluchao.reggie.entity.Setmeal;
import com.itluchao.reggie.entity.SetmealDish;
import com.itluchao.reggie.mapper.SetmealMapper;
import com.itluchao.reggie.service.SetmealDishService;

@Service
public class SetmealService extends ServiceImpl<SetmealMapper, Setmeal>
        implements com.itluchao.reggie.service.SetmealService {
    @Autowired
    SetmealDishService setmealDishService;

    // 添加菜品套餐
    @Transactional
    public void Insertmeal(SetmealDto setmealDto) {
        // 保存到套餐表
        this.save(setmealDto);
        // 保存到套餐菜品关系表,一个套餐包含多个菜品
        List<SetmealDish> list = setmealDto.getSetmealDishes();

        // 把setmeal id赋值
        list = list.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(list);
    }

}
