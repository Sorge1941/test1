package com.itluchao.reggie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itluchao.reggie.entity.Category;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
/* 分类管理 */
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // 添加菜品
    @PostMapping("")
    public R AddCategory(@RequestBody Category category) {

        categoryService.AddCategory(category);
        return R.success(null);
    }

    // 分页查询
    @GetMapping("/page")
    public R<Page> getCategory(HttpServletRequest request,
            Integer page,
            Integer pageSize) {
        System.out.println(page);
        // 条件构造器
        QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
        // 分页构造器
        Page<Category> p = new Page<>(page, pageSize);
        // 排序
        queryWrapper.orderByAsc("sort");

        categoryService.page(p, queryWrapper);
        return R.success(p);
    }

    // 删除分类
    @DeleteMapping("")
    public R<String> deleteCategory(HttpServletRequest request, long ids) {
        categoryService.deleteCategory(ids);
        return R.success(null);
    }


    //修改分类
    @PutMapping("")
    public R ChangeCategory(@RequestBody Category category) {

        categoryService.updateById(category);
        return R.success(null);
    }


    //返回菜品信息
    @GetMapping("/list")
    public R<List<Category>> fanhui(Category category){
        //查询构造器
        QueryWrapper<Category> queryWrapper=new QueryWrapper<Category>();
        if(category.getType()!=null)
        queryWrapper.eq("type",category.getType());
        //排序
        queryWrapper.orderByAsc("sort");
        
        List<Category> list=categoryService.list(queryWrapper);
        return R.success(list);

    }
    }
    
