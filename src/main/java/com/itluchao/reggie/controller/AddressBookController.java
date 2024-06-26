package com.itluchao.reggie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itluchao.reggie.entity.AddressBook;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.exception.BaseContext;
import com.itluchao.reggie.service.AddressBookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
@Autowired
AddressBookService addressBookService;

//查询收货地址
@GetMapping("/list")
public R<List<AddressBook>> list(){
    //获取用户id
    Long userid=BaseContext.getCurrentId();
    //根据id获取地址，然后上传
    QueryWrapper<AddressBook> queryWrapper=new QueryWrapper<AddressBook>();
    queryWrapper.eq("user_id",userid);

    List<AddressBook> list=addressBookService.list(queryWrapper);
    return R.success(list);
}

//添加收货地址
@PostMapping("")
public R<String> add(@RequestBody AddressBook addressBook){
 //获取用户id
 Long userid=BaseContext.getCurrentId();
addressBook.setUserId(userid);

addressBookService.save(addressBook);
return R.success("添加成功");
}


//设置默认地址
@PutMapping("/default")
public R<String> SetDefault(@RequestBody AddressBook addressBook){
    //先查询一下有没有地址是默认的
    Long userId=BaseContext.getCurrentId();
    //设置查询条件
    QueryWrapper<AddressBook> queryWrapper=new QueryWrapper<AddressBook>();
    queryWrapper.eq("user_id",userId);
    queryWrapper.eq("is_default",1);
    AddressBook defaultAddress=addressBookService.getOne(queryWrapper);
    
    if(defaultAddress==null){//没有，s获取默认地址的id
        addressBook.setIsDefault(1);
        //根据Id更新
        addressBookService.updateById(addressBook);
    }
    else{
        //有的话修改原来的,把新的设为默认
        defaultAddress.setIsDefault(0);
        addressBookService.updateById(defaultAddress);
        addressBook.setIsDefault(1);
        //根据Id更新
        addressBookService.updateById(addressBook);
    }
    return R.success("设置成功");
}

//获取默认地址
@GetMapping("/default")
public R<AddressBook> GetAddress(){
    Long userId=BaseContext.getCurrentId();
    //设置查询条件
   
    QueryWrapper<AddressBook> queryWrapper=new QueryWrapper<AddressBook>();
    AddressBook defaultAddress=new AddressBook();
    //获取默认地址
    queryWrapper.eq("user_id",userId);
    queryWrapper.eq("is_default",1);
    defaultAddress=addressBookService.getOne(queryWrapper);

    return R.success(defaultAddress);
}
}
