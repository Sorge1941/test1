package com.itluchao.reggie.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itluchao.reggie.entity.R;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;




@RestController
@RequestMapping("/common")
public class CommonController {
   
    private String path="T:\\A\\Reggie\\1 瑞吉外卖项目\\资料\\图片资源\\";
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IllegalStateException, IOException{
        //获取原始文件名后缀
        String suffix=file.getOriginalFilename();
        String suffix1=suffix.substring(suffix.lastIndexOf("."));


        String filename=UUID.randomUUID().toString()+suffix1;


        file.transferTo(new File(path+filename));
        return R.success(filename);
    }

    //文件下载上传
    @GetMapping("/download")
    public void download(String name,HttpServletResponse response) throws IOException{
        //要上传什么类型的数据

        //读取文件内容
        FileInputStream fileInputStream=new FileInputStream(new File(path+name));

        //输出流
        ServletOutputStream servletOutputStream=response.getOutputStream();
        

        //输出
        byte[] bytes=new byte[1024];
        int len=0;
        while ((len=fileInputStream.read(bytes))!=-1) {
            servletOutputStream.write(bytes,0,len);
            servletOutputStream.flush();
        }
        fileInputStream.close();
        servletOutputStream.close();
    }
    

}
