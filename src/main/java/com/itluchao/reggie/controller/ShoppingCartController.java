package com.itluchao.reggie.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itluchao.reggie.entity.Category;
import com.itluchao.reggie.entity.Dish;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.entity.ShoppingCart;
import com.itluchao.reggie.exception.BaseContext;
import com.itluchao.reggie.service.CategoryService;
import com.itluchao.reggie.service.DishService;
import com.itluchao.reggie.service.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    //查询购物车
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpServletRequest request){
        QueryWrapper<ShoppingCart> queryWrapper=new QueryWrapper<ShoppingCart>();
        queryWrapper.eq("user_id",(Long)request.getSession().getAttribute("user"));
        queryWrapper.orderByAsc("create_time");
        
        List<ShoppingCart> list=shoppingCartService.list(queryWrapper);

        return R.success(list);
    }
    
    //添加到购物车
    @PostMapping("/add")
    public R<ShoppingCart> add( HttpServletRequest request, @RequestBody ShoppingCart shoppingCart){
        //设置id，是哪个用户的
        Long userid=(Long) request.getSession().getAttribute("user");//获得session里的
        shoppingCart.setUserId(userid);
        //判断购物车里有没有
        //有+1

        //没有，插入
        //是套餐还是菜品
        ShoppingCart result=new ShoppingCart();
        QueryWrapper<ShoppingCart> queryWrapper=new QueryWrapper<ShoppingCart>();
        if(shoppingCart.getDishId()!=null){
            //根据两个id查询
            queryWrapper.eq("dish_id",shoppingCart.getDishId());
            queryWrapper.eq("user_id",shoppingCart.getUserId());

            result=shoppingCartService.getOne(queryWrapper);
        }
        else{
            //根据两个id查询
            queryWrapper.eq("setmeal_id",shoppingCart.getDishId());
            queryWrapper.eq("user_id",shoppingCart.getUserId());

            result=shoppingCartService.getOne(queryWrapper);
        }
        if(result==null){
            //没有，直接插入
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }
        else{
            //有，+1
            result.setNumber(result.getNumber()+1);
            shoppingCartService.updateById(result);
        }

        return R.success(result);
    }


    //删除购物车
    @PostMapping("/sub")
    public R<ShoppingCart> sub(HttpServletRequest request, @RequestBody ShoppingCart shoppingCart){
        //设置id，是哪个用户的
        Long userid=(Long) request.getSession().getAttribute("user");//获得session里的
        shoppingCart.setUserId(userid);
        //判断购物车里有没有
        //有1个，直接删除，大于1个就减1

        //是套餐还是菜品
        ShoppingCart result=new ShoppingCart();
        QueryWrapper<ShoppingCart> queryWrapper=new QueryWrapper<ShoppingCart>();
        if(shoppingCart.getDishId()!=null){
            //根据两个id查询
            queryWrapper.eq("dish_id",shoppingCart.getDishId());
            queryWrapper.eq("user_id",shoppingCart.getUserId());

            result=shoppingCartService.getOne(queryWrapper);
        }
        else{
            //根据两个id查询
            queryWrapper.eq("setmeal_id",shoppingCart.getDishId());
            queryWrapper.eq("user_id",shoppingCart.getUserId());

            result=shoppingCartService.getOne(queryWrapper);
        }
        if(result.getNumber()==1){
            //有一个，直接删除
            shoppingCartService.removeById(result);
        }
        else{
            //-1
            result.setNumber(result.getNumber()-1);
            shoppingCartService.updateById(result);
        }

        return R.success(result);
    }


    //clean
    @DeleteMapping("/clean")
    public R<String> clean(HttpServletRequest request){
        Long id=(Long) request.getSession().getAttribute("user");
        QueryWrapper<ShoppingCart> queryWrapper=new QueryWrapper<ShoppingCart>();
        queryWrapper.eq("user_id",id);
        //根据id删除
        shoppingCartService.remove(queryWrapper);
        return R.success("清空成功");
    }
} 