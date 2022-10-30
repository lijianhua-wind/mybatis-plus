package com.lijianhua.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijianhua.mybatisplus.domain.Product;
import com.lijianhua.mybatisplus.domain.User;
import com.lijianhua.mybatisplus.mapper.ProductMapper;
import com.lijianhua.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 李建华
 * @create 2022/10/28 21:14
 * @description 自己写
 * @since 1.0
 */
@SpringBootTest
public class MybatisPlusPluginsTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    //1. 测试分页插件
    @Test
    void testPage() {
        //设置为第一页每页显示3条，计算公式 limit (pageNo - 1) * pageSize, pageSize
        Page<User> page = new Page<>(2, 3);
        userMapper.selectPage(page, null);
        List<User> records =
                page.getRecords();
        records.forEach(System.out::println);
        /*  执行过程
        ==>  Preparing: SELECT COUNT(*) AS total FROM t_user WHERE is_deleted = 0
        ==> Parameters:
        <==    Columns: total
        <==        Row: 6
        <==      Total: 1
        ==>  Preparing: SELECT uid AS id,user_name AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 LIMIT ?,?
        ==> Parameters: 3(Long), 3(Long)
        <==    Columns: id, name, age, email, is_deleted
        <==        Row: 4, Sandy, 21, test4@baomidou.com, 0
        <==        Row: 5, Billie, 24, test5@baomidou.com, 0
        <==        Row: 999, 李四, 999, lisi@qq.com, 0
        <==      Total: 3
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@45c2e0a6]
        User(id=4, name=Sandy, age=21, email=test4@baomidou.com, isDeleted=0)
        User(id=5, name=Billie, age=24, email=test5@baomidou.com, isDeleted=0)
        User(id=999, name=李四, age=999, email=lisi@qq.com, isDeleted=0)
         */
        //分页相关数据：
        System.out.println("分页内容：" + page.getRecords());
        System.out.println("当前页码：" + page.getCurrent());
        System.out.println("总行数：" + page.getTotal());
        System.out.println("总页码：" + page.getPages());
        System.out.println("获取设置的pageSize：" + page.getSize());
        System.out.println("是否存在下一页：" + page.hasNext());
        System.out.println("是否存在上一页：" + page.hasPrevious());
    }

    //2. 测试自己自定义的page方法
    @Test
    void testCustomerPageMethod() {
        Page<User> userPage = userMapper.selectPageVo(new Page<>(1, 3), 19);
        userPage.getRecords().forEach(System.out::println);
    }

    //3. 乐观锁
    /*
    一件商品，成本价是80元，售价是100元。老板先是通知小李，说你去把商品价格增加50元。小
    李正在玩游戏，耽搁了一个小时。正好一个小时后，老板觉得商品价格增加到150元，价格太
    高，可能会影响销量。又通知小王，你把商品价格降低30元。
    此时，小李和小王同时操作商品后台系统。小李操作的时候，系统先取出商品价格100元；小王
    也在操作，取出的商品价格也是100元。小李将价格加了50元，并将100+50=150元存入了数据
    库；小王将商品减了30元，并将100-30=70元存入了数据库。是的，如果没有锁，小李的操作就
    完全被小王的覆盖了。
    现在商品价格是70元，比成本价低10元。几分钟后，这个商品很快出售了1千多件商品，老板亏1
    万多。

    乐观锁与悲观锁
            上面的故事，如果是乐观锁，小王保存价格前，会检查下价格是否被人修改过了。如果被修改过
        了，则重新取出的被修改后的价格，150元，这样他会将120元存入数据库。
        如果是悲观锁，小李取出数据后，小王只能等小李操作完之后，才能对价格进行操作，也会保证
        最终的价格是120元。
     */
    //未使用乐观锁的情况
    @Test
    void testProduct01() {
        //注意：商品原价为100
        //1. 小李查询商品价格 100
        Product productLi = productMapper.selectById(1);
        System.out.println("小李查询的商品价格：" + productLi.getPrice());
        //2. 小王查询商品价格 100
        Product productWang = productMapper.selectById(1);
        System.out.println("小王查询的商品价格：" + productWang.getPrice());
        //3. 小李将商品价格 + 50 = 150
        productLi.setPrice(productLi.getPrice() + 50);
        productMapper.updateById(productLi);
        //4. 小王将商品价格 - 30 = 70
        productWang.setPrice(productWang.getPrice() - 30);
        productMapper.updateById(productWang);
        //5. 老板查询商品价格 = 70
        Product productLaoBan = productMapper.selectById(1);
        //老板查询的商品价格：70，老板预期价格为120，就是先 + 50 后 - 30
        System.out.println("老板查询的商品价格：" + productLaoBan.getPrice());
    }

    //使用了mybatis-plus的乐观锁插件
    @Test
    void testProduct02() {
        //注意：商品原价为100
        //1. 小李查询商品价格 100
        Product productLi = productMapper.selectById(1);
        System.out.println("小李查询的商品价格：" + productLi.getPrice());
        /*
        ==>  Preparing: SELECT id,name,price,version FROM t_product WHERE id=?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, price, version
        <==        Row: 1, 外星人笔记本, 100, 0
        <==      Total: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@1000d54d]
        小李查询的商品价格：100
         */
        //2. 小王查询商品价格 100
        Product productWang = productMapper.selectById(1);
        System.out.println("小王查询的商品价格：" + productWang.getPrice());
        /*
        ==>  Preparing: SELECT id,name,price,version FROM t_product WHERE id=?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, price, version
        <==        Row: 1, 外星人笔记本, 100, 0
        <==      Total: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@4601203a]
        小王查询的商品价格：100
         */
        //3. 小李将商品价格 + 50 = 150
        productLi.setPrice(productLi.getPrice() + 50);
        productMapper.updateById(productLi);
        /*  版本相同，修改成功
        ==>  Preparing: UPDATE t_product SET name=?, price=?, version=? WHERE id=? AND version=?
        ==> Parameters: 外星人笔记本(String), 150(Integer), 1(Integer), 1(Long), 0(Integer)
        <==    Updates: 1
         */
        //4. 小王将商品价格 - 30 = 70，失败
        productWang.setPrice(productWang.getPrice() - 30);
        int result = productMapper.updateById(productWang);
        /*
        ==>  Preparing: UPDATE t_product SET name=?, price=?, version=? WHERE id=? AND version=?
        ==> Parameters: 外星人笔记本(String), 70(Integer), 2(Integer), 1(Long), 1(Integer)
        <==    Updates: 0
         */
        if (result == 0) {
            //修改失败，再次修改
            productWang = productMapper.selectById(1);  //重新获取最新数据
            /*
            ==>  Preparing: SELECT id,name,price,version FROM t_product WHERE id=?
            ==> Parameters: 1(Integer)
            <==    Columns: id, name, price, version
            <==        Row: 1, 外星人笔记本, 150, 2
            <==      Total: 1
             */
            productWang.setPrice(productWang.getPrice() - 30);
            productMapper.updateById(productWang);
            /*
                ==>  Preparing: UPDATE t_product SET name=?, price=?, version=?
                     WHERE id=? AND version=?
                ==> Parameters: 外星人笔记本(String), 120(Integer),
                                3(Integer), 1(Long), 2(Integer)
                <==    Updates: 1
             */
        }
        /*
        ==>  Preparing: UPDATE t_product SET name=?, price=?, version=? WHERE id=? AND version=?
        ==> Parameters: 外星人笔记本(String), 70(Integer), 1(Integer), 1(Long), 0(Integer)
        <==    Updates: 0
         */
        //5. 老板查询商品价格 = 120，因为使用了乐观锁
        Product productLaoBan = productMapper.selectById(1);
        //老板查询的商品价格：120，老板预期价格为120，就是先 + 50 后 - 30
        System.out.println("老板查询的商品价格：" + productLaoBan.getPrice());
        /*
        ==>  Preparing: SELECT id,name,price,version FROM t_product WHERE id=?
        ==> Parameters: 1(Integer)
        <==    Columns: id, name, price, version
        <==        Row: 1, 外星人笔记本, 120, 3
        <==      Total: 1
        Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@63dfada0]
        老板查询的商品价格：120
         */
    }
}
