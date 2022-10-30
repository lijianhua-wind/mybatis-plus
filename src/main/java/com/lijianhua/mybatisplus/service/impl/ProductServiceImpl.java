package com.lijianhua.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijianhua.mybatisplus.domain.Product;
import com.lijianhua.mybatisplus.mapper.ProductMapper;
import com.lijianhua.mybatisplus.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * @author 李建华
 * @create 2022/10/28 22:09
 * @description 自己写
 * @since 1.0
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
