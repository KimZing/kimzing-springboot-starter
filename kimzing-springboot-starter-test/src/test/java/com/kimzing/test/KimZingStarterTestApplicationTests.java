package com.kimzing.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class KimZingStarterTestApplicationTests {

    @Resource
    MockMvc mockMvc;

    @Test
    @DisplayName(value = "测试日志打印，从控制台人工验证是否成功")
    public void printLogTest(TestInfo info) throws Exception {
        log.info(">>>开始测试:{}", info.getDisplayName());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0"));
        log.info("<<<<结束测试:{}", info.getDisplayName());
    }

    @Test
    @DisplayName(value = "测试在Get请求中发送json的url参数")
    public void jsonGetMethod(TestInfo info) throws Exception {
        log.info(">>>开始测试:{}", info.getDisplayName());
        String data = "{\"ageFrom\":\"18\",\"ageTo\":\"24\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .get("/user/list/condition?{data}", data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("0"));
        log.info("<<<<结束测试:{}", info.getDisplayName());
    }

    @Test
    @DisplayName(value = "测试异常拦截")
    public void exceptionAdviceTest(TestInfo info) throws Exception {
        log.info(">>>开始测试:{}", info.getDisplayName());
        mockMvc.perform(MockMvcRequestBuilders.get("/user/-1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("用户ID异常"));
        log.info("<<<<结束测试:{}", info.getDisplayName());
    }

}
