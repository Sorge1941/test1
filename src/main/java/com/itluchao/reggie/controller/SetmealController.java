package com.itluchao.reggie.controller;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itluchao.reggie.dto.SetmealDto;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.entity.Setmeal;
import com.itluchao.reggie.entity.SetmealDish;
import com.itluchao.reggie.exception.CustomException;
import com.itluchao.reggie.service.CategoryService;
import com.itluchao.reggie.service.SetmealDishService;
import com.itluchao.reggie.service.impl.SetmealService;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import jakarta.websocket.server.PathParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    SetmealService setmealService;

    @Autowired
    CategoryService categoryService;

    // 新增套餐
    @PostMapping("")
    public R<String> Insertmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.Insertmeal(setmealDto);
        return R.success("保存成功");
    }

    // 分页查询
    @GetMapping("/page")
    public R<Page> pageShow(Integer page, Integer pageSize, String name) {
        // 设置查询条件
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<Setmeal>();
        queryWrapper.orderByDesc("update_time");

        if (name != null)
            queryWrapper.like("name", name);

        // 设置分页条件
        Page<Setmeal> p = new Page<>(page, pageSize);
        Page<SetmealDto> pdao = new Page<>();

        setmealService.page(p, queryWrapper);
        // 为什么不能用pdao直接查，元素不一样
        // 赋值一下，
        BeanUtils.copyProperties(p, pdao, "records");// 查出来的记录暂时不用
        // 要把套餐分类查出来
        List<Setmeal> list = p.getRecords();// 先获取里边的记录
        List<SetmealDto> listEnd = list.stream().map(item -> {
            // 定义一个Setmealdto
            SetmealDto setmealDto = new SetmealDto();
            // 赋值
            BeanUtils.copyProperties(item, setmealDto);

            // 分类的名字

            setmealDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());

            return setmealDto;
        }).collect(Collectors.toList());

        pdao.setRecords(listEnd);
        return R.success(pdao);
    }

    // 删除
    @Transactional
    @DeleteMapping("")
    public R<String> DeleteSetmeal(@RequestParam List<Long> ids) {

        for (Long id : ids) {
            // 要删除setmeal
            QueryWrapper<Setmeal> queryWrapper2 = new QueryWrapper<Setmeal>();

           
            queryWrapper2.eq("id", id);
            Setmeal setmeal=setmealService.getOne(queryWrapper2);
            if(setmeal.getStatus()==0)//如果状态为停售，则删除
            setmealService.remove(queryWrapper2);
            else{
                throw new CustomException("请先停售");
            }


            // 要删除setmeal dish
            QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<SetmealDish>();
            queryWrapper.eq("setmeal_id", id);

            setmealDishService.remove(queryWrapper);

        }
        return R.success("删除成功");
    }

    // 批量停售和起售

    @PostMapping("/status/{status}")
    public R<String> start(@PathVariable Integer status, @RequestParam List<Long> ids) {
        // 要修改setmeal
        for (Long id : ids) {
            UpdateWrapper<Setmeal> qUpdateWrapper = new UpdateWrapper<Setmeal>();
            qUpdateWrapper.eq("id", id);
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);

            setmealService.update(setmeal, qUpdateWrapper);
        }
        return R.success("修改成功");

    }

    //返回查询
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        QueryWrapper<Setmeal> queryWrapper=new QueryWrapper<Setmeal>();
        queryWrapper.eq("category_id",setmeal.getCategoryId());
        queryWrapper.eq("status",1);

        List<Setmeal> list=setmealService.list(queryWrapper);
        //steaml里的id没赋值


        return R.success(list);
    }
    }
    



