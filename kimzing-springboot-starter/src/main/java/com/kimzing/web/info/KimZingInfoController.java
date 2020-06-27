package com.kimzing.web.info;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 项目基本信息Rest接口.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/26 13:33
 */
@Data
@RestController
public class KimZingInfoController {

    /**
     * 响应内容
     */
    private Map<String, Object> infoMap;

    @GetMapping(value = "${kimzing.web.info.path:/info}")
    public Map<String, Object> info() {
        return infoMap;
    }

}
