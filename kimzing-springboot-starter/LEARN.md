# 使用文档

**使用本文档中的工具前记得先进行依赖引入: [我在这里](README.md)**


* [使用文档](#使用文档)
  * :cherries:打印方法执行日志
    * [功能介绍](#功能介绍)
    * [功能配置](#功能配置)
    * [使用方式](#使用方式)
    * [使用示例](#使用示例)
  * :cherries:读取Spring容器内属性
    * [功能介绍](#功能介绍-1)
    * [功能配置(可选)](#功能配置(可选))
    * [使用方式](#使用方式-1)
    * [使用示例](#使用示例-1)
  * :cherries:Spring上下文工具
    * [功能介绍](#功能介绍-2)
    * [使用方式](#使用方式-2)
    * [使用示例](#使用示例-2)
  * :cherries:在Get请求使用Json进行传参
    * [功能介绍](#功能介绍-3)
    * [使用方式](#使用方式-3)
    * [使用示例](#使用示例-3)

## :cherries:打印方法执行日志 `LogAspect.java`

### 功能介绍

打印方法的入参、返回值、执行时间、异常信息等信息

### 功能配置

```yaml
kimzing:
  log:
    enabled: true # 开启方法执行信息收集功能
    time-pattern: yyyy-MM-dd HH:mm:ss # 配置方法信息中时间的格式
```

### 使用方式

* 使用默认实现`DefaultLogAspect.java`，打印方法执行信息

在需要进行日志打印的方法上加上`@LogKim(desc = "示例")`注解即可，方法执行时将会在控制台打印对应的执行信息。desc为该方法的描述信息，建议填写。

```java
public class Demo {
    @LogKim(desc = "示例方法")
    public String method() {
        return "hello";
    }
}
```

* 自定义对方法执行信息的处理方式

编写自定义的容器类，继承`LogAspect`, 实现`handleLogInfo(LogInfo logInfo)`方法，`LogInfo`为对应的方法执行信息。
handleLogInfo方法中可以进行自定义处理，例如对方法执行信息进行持久化、落库、分析等。

```java
@Component
public class JsonLogAspect extends LogAspect {

    @Override
    public void handleLogInfo(LogInfo logInfo) {
        sendToES(logInfo);
    }
}
```

### 使用示例

自定义处理类: 使用Json格式进行日志打印: `JsonLogAspect`

方法标记示例: `UserController`

## :cherries:读取Spring容器内属性 `SpringPropertyUtil.java`

### 功能介绍

读取Spring容器内属性，包含`application-*.yml`、@PropertySource加载的所有Spring属性，通过`key`获取`value`

### 功能配置(可选)

自定义需要加载的配置文件, 配置文件中的属性将会被装入Spring容器属性中。

```yaml
kimzing:
  property:
    files: ["message.properties","exception.properties"]
```

### 使用方式

```java
public class SpringPropertyUtilTest {

    /**
     * 获取容器内属性，如果不存在，返回null
     */
    public void testGetPropertyWhenKeyIsNotExist() {
        String value = SpringPropertyUtil.getValue("1001");
        // value = "test-message"

        String value = SpringPropertyUtil.getValue("not-exist");
        // value = null

        String defaultValue = "default message";
        String value = SpringPropertyUtil.getValueWithDefault("not-exist", defaultValue);
        // value = "default message"
    }
}
```

### 使用示例

`SpringPropertyUtilTest`

## :cherries:Spring上下文工具 `SpringContextUtil.java`

### 功能介绍

通过ApplicationContext进行容器操作，主要用来在非容器类中获取Spring管理的容器Bean，以及获取ApplicationContext实例(用来发布事件等操作)。

### 使用方式

```java
public class Demo {
    
    public void getInstance() {
        // 获取单例对象
        UserService userService = SpringContextUtil.getSingleBeanByClass(UserService.class);
        // 包装多个相同类型的实例为Map
        Map<String, UserService> userServiceMap = SpringContextUtil.getBeansByTypeToMap(UserService.class);
        // 获取Spring上下文
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        // ...
    }

}
```

### 使用示例

`SpringContextUtilTest`

## :cherries:在Get请求中使用Json进行传参

### 功能介绍

在Restful风格的url中，要求使用get方式进行资源获取，然而在复杂查询时，使用get方式进行参数传递就不是太方便了，所以封装了get携带json参数的方式，会更加优雅一些。

查询用户示例: GET http://localhost:8080/user?{"ageFrom":18,"ageTo":24,"gender":"MAN"}

### 使用方式

- 客户端: 发送Get查询请求时使用`GET http://localhost:8080/user?{}`的方式进行传参。
- 服务端: 在接收的参数体上面加上@JsonParam注解即可

### 使用示例

`UserController#listByCondition`

## :cherries:项目基础信息接口

### 功能介绍

### 功能配置

### 使用方式

### 使用示例

## :cherries: SpringMVC全局异常切面管理

### 功能介绍

### 功能配置

### 使用方式

### 使用示例

## :cherries: 属性映射工具

### 功能介绍

### 使用方式

### 使用示例

## :cherries: LocalDateTime时间工具

### 功能介绍

### 使用方式

### 使用示例

## :cherries: 自定义异常及异常管理工具

### 功能介绍

### 使用方式

### 使用示例

## :cherries: 日志打印工具

### 功能介绍

### 使用方式

### 使用示例

## :cherries: 分页工具-Mybatis

### 功能介绍

### 使用方式

### 使用示例

## :cherries: 分页工具-Hibernate

### 功能介绍

### 使用方式

### 使用示例

## :cherries: 数据实体公共属性定义

### 功能介绍

### 使用方式

### 使用示例

## :cherries: 统一的接口返回体

### 功能介绍

### 使用方式

### 使用示例


