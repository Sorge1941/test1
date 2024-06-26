package com.itluchao.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itluchao.reggie.entity.Category;

public interface CategoryService extends IService<Category>{
public void AddCategory(Category category);



//删除员工
public void deleteCategory(long id);



}
