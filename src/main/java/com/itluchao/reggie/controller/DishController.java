package com.itluchao.reggie.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itluchao.reggie.dto.DishDto;
import com.itluchao.reggie.entity.Category;
import com.itluchao.reggie.entity.Dish;
import com.itluchao.reggie.entity.DishFlavor;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.service.CategoryService;
import com.itluchao.reggie.service.DishFlavorService;
import com.itluchao.reggie.service.DishService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

//菜品管理
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;
    // 查询菜品
    @GetMapping("/page")
    public R<Page> getDish(HttpServletRequest request, Integer page, Integer pageSize, String name) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<Dish>();
        if (name != null)
            queryWrapper.like("name", name);
        // 根据sort排序
        queryWrapper.orderByAsc("sort");
        // 分页
        Page<Dish> p = new Page<>(page, pageSize);
        // 查询D
        // dishdto才可以满足返回的要求
        Page<DishDto> pto = new Page<>();
        dishService.page(p, queryWrapper);

        // 复制pto,//不复制records,因为要修改
        BeanUtils.copyProperties(p, pto, "records");
        // 得到列表数据
        List<Dish> list = p.getRecords();
        // 查到分类后赋给pto
        List<DishDto> listDto = list.stream()
                .map(item -> {
                    // 获取名字
                    DishDto dishDto = new DishDto();
                    // 把item里的东西复制一下
                    BeanUtils.copyProperties(item, dishDto);
                    // 得到名字
                    dishDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
                    return dishDto;
                })
                .collect(Collectors.toList());

        pto.setRecords(listDto);
        return R.success(pto);
    }

    // 新建菜品
    @PostMapping("")
    public R<String> AddDish(@RequestBody DishDto dishDto) {
        // 怎么？？
        dishService.AddDish(dishDto);
        //清理缓存数据
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("新增成功");
    }

    // 回显
    @GetMapping("/{id}")
    public R<DishDto> changeDish(@PathVariable long id) {

        // 先查出来
        Dish dish = dishService.getById(id);
        DishDto dishDto = new DishDto();
        // 复制一下
        BeanUtils.copyProperties(dish, dishDto);

        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<DishFlavor>();
        queryWrapper.eq("dish_id", dish.getId());
        // 要把Flavors找出来
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        // 获取名字
        dishDto.setCategoryName(categoryService.getById(dish.getCategoryId()).getName());
        return R.success(dishDto);
    }

    // 修改菜品
    @PutMapping("")
    public R<String> updateDish(@RequestBody DishDto dishDto) {
        // 修改
        dishService.updateDish(dishDto);
        //清理缓存数据
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        return R.success("修改成功");
    }

    // 起售和停售
    @PostMapping("status/{status}")
    public R<String> start(@PathVariable Integer status, @RequestParam long ids) {
        dishService.stop(status, ids);
        return R.success("成功");

    }

    // 删除菜品
    @DeleteMapping("")
    public R<String> DeleteDish(@RequestParam long ids) {

        dishService.DeleteDish(ids);
        return R.success("删除成功");
    }

    // 返回套餐菜品
    @GetMapping("/list")
    public R<List<DishDto>> fanhui(Dish dish){
        List<DishDto> dto=null;
        //设置key
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();

        //从redis获取数据
        dto=  (List<DishDto>) redisTemplate.opsForValue().get(key);
        if(dto!=null){//取到数据，不用查数据库
            return R.success(dto);
        } 
        //编辑查询条件
        QueryWrapper<Dish> queryWrapper=new QueryWrapper<Dish>();
        if(dish.getCategoryId()!=0)
        queryWrapper.eq("category_id",dish.getCategoryId());
        
        queryWrapper.eq("status",1);

        //查询返回为列表,不能用DishDto直接查
        List<Dish> list=dishService.list(queryWrapper);
        //创建一个List<dto>
        dto= list.stream().map(item->{
            DishDto dishDto=new DishDto();
            //复制一下
            BeanUtils.copyProperties(item,dishDto);
            //要favlor找出来
            QueryWrapper<DishFlavor> queryWrapper2=new QueryWrapper<DishFlavor>();
            queryWrapper2.eq("dish_id",item.getId());

             List<DishFlavor> flavors=dishFlavorService.list(queryWrapper2);
             //赋值
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        //第一次查询赋值
        redisTemplate.opsForValue().set(key,dto,60,TimeUnit.MINUTES);


        return R.success(dto);
    }


    
}

