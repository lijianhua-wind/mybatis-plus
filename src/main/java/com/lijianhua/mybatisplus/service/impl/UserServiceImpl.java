package com.lijianhua.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.mapper.UserMapper;
import com.lijianhua.mybatisplus.service.UserService;
import org.springframework.stereotype.Service;


/**
 * @author 李建华
 * @date 2022/10/27
 * @comment
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
