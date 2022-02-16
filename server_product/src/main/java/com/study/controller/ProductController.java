package com.study.controller;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 35612
 */
@RestController
@MapperScan("com.study.mapper")
@RequestMapping("/product")
public class ProductController {

    @Secured("ROLE_PRODUCT")
    @RequestMapping("/findAll")
    public String findAll() {
        return "产品列表";
    }
}
