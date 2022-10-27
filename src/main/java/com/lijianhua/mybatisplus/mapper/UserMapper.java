package com.lijianhua.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lijianhua.mybatisplus.domain.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Map;

@Repository
public interface UserMapper extends BaseMapper<User> {

    @MapKey("id")
    Map<String,Object> selectMapById(@Param("id") Long id);
}
