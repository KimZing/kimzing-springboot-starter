# kimzing-springboot-starter

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/KimZing/kimzing-springboot/blob/master/LICENSE)

## 项目简介

基于SpringBoot Starter封装的基础起步依赖，主要包含常用工具类、日志及其它自动化配置, 提供对应的切入点进行个性化配置

## 快速开始

首先在`pom.xml`中引入私有仓库地址

```xml
<repositories>
    <!--使用snapshot版本-->
    <repository>
        <id>KimZing-SNAPSHOT</id>
        <name>KimZing</name>
        <url>http://mvn.kimzing.com/nexus/content/repositories/snapshots/</url>
    </repository>
    <!--使用release版本-->
    <repository>
        <id>KimZing-RELEASE</id>
        <name>KimZing</name>
        <url>http://mvn.kimzing.com/nexus/content/repositories/releases/</url>
    </repository>
</repositories>
```

然后引入如下依赖刷新即可

```xml
<dependency>
    <groupId>com.kimzing</groupId>
    <artifactId>kimgzing-springboot-starter</artifactId>
    <version>${version}</version>
</dependency>
```

:eyes:[SNAPSHOT版本](http://mvn.kimzing.com/nexus/content/repositories/snapshots/com/kimzing/kimzing-springboot-starter/)

## 项目使用

:green_book:[使用文档](https://github.com/KimZing/kimzing-springboot-starter/blob/master/kimzing-springboot-starter/LEARN.md)

## 结构说明

```bash
└── kimzing
    ├── autoconfigure     # springboot自动配置类
    │   └── properties    # 自动装配的属性类
    ├── exception         # 自定义异常
    ├── log               # 方法日志切面配置
    │   └── impl          # 日志切面默认实现
    ├── utils             # 通用工具类
    │   ├── spring        # 与spring容器相关的工具类
    │   └── ...           # 相对独立的工具类，可以脱离spring项目使用
    └── web               # springboot-web相关自动配置
        ├── advice        # controller异常切面处理
        ├── info          # 项目基础信息 /info 接口配置
        └── resolver      # 参数解析器
```

## 脚本命令

- 本地安装(cmd/install.sh)

```bash
mvn install -Dmaven.test.skip=true
```

- 上传远程仓库(cmd/deploy.sh)

```bash
mvn deploy -Dmaven.test.skip=true
```

## 项目规约

### 版本

* 版本采用x.y.z的规约进行命名，release版本使用`-RELEASE`标记，snapshot版本使用`-SNAPSHOT`标记。
* master分支为当前最新功能分支
* 不同的版本切出不同的分支进行记录

### 扩展性

* 所有针对SpringBoot做的自动化配置必须做到可关闭，可扩展

### 可用性

* `kimzing-springboot-starter`必须在对应的`kimzing-springboot-starter-test`项目进行标准测试

## TODO

- [ ] 完善异常机制
- [ ] Mybatis/Hibernate分页包装
- [ ] 请求链路中添加请求id
- [ ] 添加ApiResult 定义统一返回体
- [ ] http api接口统一提供rest文件
- [ ] 使用guava  commons
- [ ] 添加跨域配置

## FAQ

### 在Controller方法中注入了ServletHttpResponse, 对OutputStream或writer进行操作后，请求报错

这个问题可能是因为在打印日志时已经进行相关操作，导致第二次读取失败，可以尝试设置`kimzing.log.enabled=true`

### 在打印日志时报错`write javaBean error, fastjson version 1.2.62`

可以检查代码中是否使用了FastJson等工具对`LogInfo`进行了toJsonString操作

## 关于更多

[:bus:更新记录](CHANGELOG.md)  :bus:[自制Starter](doc/make-starter.md)

## 赞助

如果你喜欢此项目并且它对你确实有帮助，欢迎给我打赏一杯:coffee:~ 

:chicken: just for fun ~

**支付宝**

![](http://images.kimzing.com/images/public/alipay-197x197.png)

**微信**

![](http://images.kimzing.com/images/public/wechatpay-197x197.png)
