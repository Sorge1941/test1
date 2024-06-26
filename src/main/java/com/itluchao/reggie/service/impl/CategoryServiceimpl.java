package com.itluchao.reggie.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itluchao.reggie.mapper.CategoryMapper;
import com.itluchao.reggie.mapper.DishMapper;
import com.itluchao.reggie.mapper.SetmealMapper;
import com.itluchao.reggie.service.CategoryService;
import com.itluchao.reggie.entity.Category;
import com.itluchao.reggie.entity.Dish;
import com.itluchao.reggie.entity.Setmeal;
import com.itluchao.reggie.exception.CustomException;
@Service
public class CategoryServiceimpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService{
@Autowired
CategoryMapper categoryMapper;
@Autowired
DishMapper dishMapper;
@Autowired
SetmealMapper setmealMapper;
@Override
public void AddCategory(Category category) {
    categoryMapper.insert(category);
}


//删除员工
@Override
public void deleteCategory(long id) {
    //加条件
    QueryWrapper<Dish> queryWrapper1=new QueryWrapper<Dish>();
    queryWrapper1.eq("category_id",id);
   //判断该id下 是否包含菜品
   Long count= dishMapper.selectCount(queryWrapper1);
   if(count>0){
        throw new CustomException("当前分类包含菜品不能删除");
   }

   QueryWrapper<Setmeal> queryWrapper=new QueryWrapper<Setmeal>();
   queryWrapper.eq("category_id",id);
   Long count1= setmealMapper.selectCount(queryWrapper);

   if(count1>0){
        throw new CustomException("当前分类包含套餐不能删除");
   }

   categoryMapper.deleteById(id);
}



}
