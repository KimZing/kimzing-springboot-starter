package com.kimzing.test.utils.spring;

import com.kimzing.test.service.UserService;
import com.kimzing.utils.spring.SpringContextUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Spring容器Bean操作.
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/1/1 18:38
 */
@SpringBootTest
public class SpringContextUtilTest {

    /**
     * 根据类型获取单个实例
     */
    @Test
    public void testGetSingleBeanByClassWhenSuccess() {
        UserService userService = SpringContextUtil.getSingleBeanByClass(UserService.class);
        Assert.isTrue(Objects.nonNull(userService), "获取UserService实例失败");
    }

    /**
     * 根据名称和类型获取单个实例
     */
    @Test
    public void testGetSingleBeanByNameAndClassWhenSuccess() {
        UserService userService = SpringContextUtil.getSingleBeanByNameAndClass("userServiceImpl", UserService.class);
        Assert.isTrue(Objects.nonNull(userService), "获取UserService实例失败");
    }

    /**
     * 根据类型获取实例的Map,
     * key:beanName
     * value: bean
     */
    @Test
    public void testGetBeansByTypeToMapWhenSuccess() {
        Map<String, UserService> userServiceMap = SpringContextUtil.getBeansByTypeToMap(UserService.class);
        Assert.isTrue(userServiceMap.size() == 1, "实例数错误");
    }

    /**
     * 根据类型获取实例集合
     */
    @Test
    public void testGetBeansByTypeToCollectionWhenSuccess() {
        Collection<UserService> userServices = SpringContextUtil.getBeansByTypeToCollection(UserService.class);
        Assert.isTrue(userServices.size() == 1, "实例数错误");
    }

    /**
     * 获取Spring上下文
     */
    @Test
    public void testGetApplicationContextWhenSuccess() {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        Assert.isTrue(Objects.nonNull(applicationContext), "容器获取失败");
    }

}
