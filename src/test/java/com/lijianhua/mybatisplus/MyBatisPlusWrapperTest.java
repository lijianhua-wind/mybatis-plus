package com.lijianhua.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @author 李建华
 * @date 2022/10/28
 * @comment
 */
@SpringBootTest
public class MyBatisPlusWrapperTest {

    @Autowired
    private UserMapper userMapper;

    //一、使用wrapper
    //1. 查询条件
    @Test
    void test01() {
        //使用queryWrapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //查询用户名包含a，年龄在20~30岁之间，邮箱信息不为null的邮箱信息
        queryWrapper.like("user_name", "a")
                .between("age", 20, 30)
                .isNotNull("email");
        List<User> users =
                userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
        /*
        ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0
        AND (user_name LIKE ? AND age BETWEEN ? AND ? AND email IS NOT NULL)
        ==> Parameters: %a%(String), 20(Integer), 30(Integer)
        <==    Columns: id, name, age, email, is_deleted
        <==        Row: 4, Sandy, 21, test4@baomidou.com, 0
        <==      Total: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@4d62f4c6]
        User(id=4, name=Sandy, age=21, email=test4@baomidou.com, isDeleted=0)
         */
    }

    //2. 排序功能
    @Test
    void test02() {
        //查询用户信息，按照年龄的降序排序，若年龄相同，则按照id升序排序
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age").orderByAsc("id");
        List<User> users =
                userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
        /*
        ==>  Preparing:
            SELECT uid AS id,user_name AS name,age,email,is_deleted
            FROM t_user
            WHERE is_deleted=0
            ORDER BY age DESC,id ASC
        ==> Parameters:
        <==    Columns: id, name, age, email, is_deleted
        <==        Row: 5, Billie, 24, test5@baomidou.com, 0
        <==        Row: 4, Sandy, 21, test4@baomidou.com, 0
        <==        Row: 6, ljh, 20, null, 0
        <==        Row: 7, ljh, 20, null, 0
        <==        Row: 8, ljh, 20, null, 0
        <==      Total: 5
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@a565cbd]
        User(id=5, name=Billie, age=24, email=test5@baomidou.com, isDeleted=0)
        User(id=4, name=Sandy, age=21, email=test4@baomidou.com, isDeleted=0)
        User(id=6, name=ljh, age=20, email=null, isDeleted=0)
        User(id=7, name=ljh, age=20, email=null, isDeleted=0)
        User(id=8, name=ljh, age=20, email=null, isDeleted=0)
         */
    }

    //3. 测试删除功能
    @Test
    void test03() {
        //删除邮箱地址为null的用户信息
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.isNull("email");
        int result = userMapper.delete(updateWrapper);
        System.out.println("result：" + result);
        /*
        ==>  Preparing: UPDATE t_user SET is_deleted=1 WHERE is_deleted=0 AND (email IS NULL)
        ==> Parameters:
        <==    Updates: 3
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@79316f3a]
        result：3
         */
    }

    //4. 测试修改功能
    //4.1 使用queryWrapper实现修改功能
    @Test
    void test04() {
        //将(年龄大于20并且用户名中包含a)或者邮箱为null的用户信息修改，修改user_name为小明
        //使用queryWrapper，queryWrapper在这里只能组装条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("age", 20).
                like("user_name", "a")
                .or().isNull("email");
        User user = new User();
        user.setName("小明");
        user.setEmail("test@qq.com");
        /*
        int update(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);
        根据 whereEntity 条件，更新记录
        Params:
        entity – 实体对象 (set 条件值,可以为 null)
        updateWrapper – 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
         */
        int result = userMapper.update(user, queryWrapper);
        System.out.println("result：" + result);
        /* 结果：
        ==>  Preparing: UPDATE t_user SET user_name=?, email=? WHERE is_deleted=0 AND (age > ? AND user_name LIKE ? OR email IS NULL)
        ==> Parameters: 小明(String), test@qq.com(String), 20(Integer), %a%(String)
        <==    Updates: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@10358c32]
        result：1
         */
    }

    //4.2 条件的优先级
    @Test
    void test5() {
        //将用户名中包含a并且（年龄大于20或邮箱为null）的用户信息修改。
        //lambda表达式中的条件优先执行
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name", "a")
                .and(qw -> qw.gt("age", 20).or().isNull("email"));
        User user = new User();
        user.setName("小红");
        user.setEmail("test@qq.com");
        int result = userMapper.update(user, queryWrapper);
        System.out.println("result：" + result);
        /*
        ==>  Preparing:
            UPDATE t_user SET user_name=?, email=?
            WHERE is_deleted=0
            AND (user_name LIKE ? AND (age > ? OR email IS NULL))
        ==> Parameters: 小红(String), test@qq.com(String), %a%(String), 20(Integer)
        <==    Updates: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@2a2843ec]
        result：1
         */
    }

    //5. 测试查询功能
    @Test
    void test07() {
        //查询用户的用户名，年龄，邮箱信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_name", "age", "email");
        List<Map<String, Object>> maps =
                userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
        /*
        ==>  Preparing: SELECT user_name,age,email FROM t_user WHERE is_deleted=0
        ==> Parameters:
        <==    Columns: user_name, age, email
        <==        Row: Jone, 18, test1@baomidou.com
        <==        Row: 小红, 20, test@qq.com
        <==        Row: Tom, 28, test3@baomidou.com
        <==        Row: 小红, 21, test@qq.com
        <==        Row: Billie, 24, test5@baomidou.com
        <==      Total: 5
         */
    }

    //5.2 实现子查询
    @Test
    void test08() {
        //查询id小于等于100的用户信息
        /*
        ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted
                        FROM t_user
                        WHERE is_deleted=0
                        AND (uid
                                IN (select uid from t_user where uid <= 100))
        ==> Parameters:
        <==    Columns: id, name, age, email, is_deleted
        <==        Row: 1, Jone, 18, test1@baomidou.com, 0
        <==        Row: 2, 小红, 20, test@qq.com, 0
        <==        Row: 3, Tom, 28, test3@baomidou.com, 0
        <==        Row: 4, 小红, 21, test@qq.com, 0
        <==        Row: 5, Billie, 24, test5@baomidou.com, 0
        <==      Total: 5
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("uid", "select uid from t_user where uid <= 100");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    //6. 使用updateWrapper实现修改功能
    @Test
    void test082() {
        //将用户名中包含a并且（年龄大于20或邮箱为null）的用户信息修改。
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.like("user_name", "a")
                .and(uw -> uw.gt("age", 20)
                        .or().isNull("email"));
        updateWrapper.set("user_name", "小黑").set("email", "abc@qq.com");
        int update = userMapper.update(null, updateWrapper);
        System.out.println("result：" + update);
        /*
        ==>  Preparing: UPDATE t_user SET user_name=?,email=?
             WHERE is_deleted=0
                        AND (user_name LIKE ? AND (age > ? OR email IS NULL))
        ==> Parameters: 小黑(String), abc@qq.com(String), %a%(String), 20(Integer)
        <==    Updates: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@76db540e]
        result：1
         */
    }

    //7. 模拟开发中条件组装的情况
    @Test
    void test09() {
        String username = "";
        Integer ageBegin = 10;
        Integer ageEnd = 30;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //如果username不为空，或者空白，加入条件
        queryWrapper.like(StringUtils.isNotBlank(username), "user_name", username)
                //年龄
                .gt(ageBegin != null, "age", ageBegin)
                .lt(ageEnd != null, "age", ageEnd);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    //8. 使用lambdaWrapper
    //重点：为什么传入User::getName就知道对应哪个字段，底层会得到 getName 这个方法名，然后转换成 name 结果就跟没使用lambda表达式一样了
    /*
        protected ColumnCache getColumnCache(SFunction<T, ?> column) {
            LambdaMeta meta = LambdaUtils.extract(column);
            String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());  //得到属性名name
            Class<?> instantiatedClass = meta.getInstantiatedClass();
            tryInitCache(instantiatedClass);
            return getColumnCache(fieldName, instantiatedClass); //得到对应的column缓存
        }
     */
    @Test
    void test10() {
        String username = "小";
        Integer ageBegin = 10;
        Integer ageEnd = 30;
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), User::getName, username)
                .ge(ageBegin != null, User::getAge, ageBegin)
                .le(ageEnd != null, User::getAge, ageEnd);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
        /*
        ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted
                        FROM t_user
                        WHERE is_deleted=0 AND (user_name LIKE ? AND age >= ? AND age <= ?)
        ==> Parameters: %a%(String), 10(Integer), 30(Integer)
        <==      Total: 0
         */
    }

    //9. 使用updateWrapper实现修改功能
    @Test
    void test0822() {
        //将用户名中包含a并且（年龄大于20或邮箱为null）的用户信息修改。
        /*
        ==>  Preparing: UPDATE t_user SET user_name=?,email=?
                        WHERE is_deleted=0
                        AND (user_name LIKE ? AND (age > ? OR email IS NULL))
        ==> Parameters: 小黑(String), abc@gmail.com(String), %a%(String), 20(Integer)
        <==    Updates: 0
         */
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.like(User::getName, "a")
                .and(uw -> uw.gt(User::getAge, 20).or().isNull(User::getEmail));
        updateWrapper.set(User::getName, "小黑").set(User::getEmail, "abc@gmail.com");
        int update = userMapper.update(null, updateWrapper);
        System.out.println("result：" + update);
    }
}
