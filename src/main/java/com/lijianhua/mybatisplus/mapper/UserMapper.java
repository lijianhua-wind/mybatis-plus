package com.lijianhua.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijianhua.mybatisplus.domain.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;

@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id查询user转换成map
     * @param id
     * @return
     */
    @MapKey("id")
    Map<String, Object> selectMapById(@Param("id") Long id);

    /**
     * 通过年龄查询用户信息并分页
     * @param page mybatis-plus所提供的分页对象，必须位于第一个参数的位置
     * @param age 查询大于age的user
     * @return
     */
    Page<User> selectPageVo(@Param("page") Page<User> page,@Param("age") Integer age);
}
