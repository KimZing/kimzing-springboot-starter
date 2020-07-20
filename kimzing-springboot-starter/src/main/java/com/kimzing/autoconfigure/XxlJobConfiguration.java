package com.kimzing.autoconfigure;

import com.kimzing.autoconfigure.properties.XxlJobProperties;
import com.kimzing.utils.log.LogUtil;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxljob自动化配置.
 *
 *
 *  针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
 *
 *       1、引入依赖：
 *           <dependency>
 *              <groupId>org.springframework.cloud</groupId>
 *              <artifactId>spring-cloud-commons</artifactId>
 *              <version>${version}</version>
 *          </dependency>
 *
 *       2、配置文件，或者容器启动变量
 *           spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
 *
 *       3、获取IP
 *           String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
 *
 *
 * @author KimZing - kimzing@163.com
 * @since 2020/7/17 01:20
 */
@Configuration
@ConditionalOnClass(value = XxlJobSpringExecutor.class)
@EnableConfigurationProperties({XxlJobProperties.class})
public class XxlJobConfiguration {

    @Bean
    @ConditionalOnMissingBean(XxlJobSpringExecutor.class)
    @ConditionalOnProperty(prefix = "kimzing.xxljob",
            name = "enabled", havingValue = "true")
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties xxlJobProperties) {
        LogUtil.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setAppname(xxlJobProperties.getExecutor().getAppname());
        xxlJobSpringExecutor.setAddress(xxlJobProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(xxlJobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getExecutor().getLogRetentionDays());

        return xxlJobSpringExecutor;
    }
}
