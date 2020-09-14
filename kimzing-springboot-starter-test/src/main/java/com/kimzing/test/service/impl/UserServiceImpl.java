package com.kimzing.test.service.impl;

import com.kimzing.test.domain.dto.UserDTO;
import com.kimzing.test.domain.po.UserPO;
import com.kimzing.test.repository.UserRepository;
import com.kimzing.test.service.UserService;
import com.kimzing.utils.bean.BeanUtil;
import com.kimzing.utils.exception.ExceptionManager;
import com.kimzing.utils.page.PageResult;
import com.kimzing.utils.result.ApiResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户模拟服务实现.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/28 16:33
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;

    @Override
    public ApiResult save(UserDTO userDTO) {
        UserPO userPO = BeanUtil.mapperBean(userDTO, UserPO.class);
        userPO = userRepository.save(userPO);
        userDTO = BeanUtil.mapperBean(userPO, UserDTO.class);
        return ApiResult.success(userDTO);
    }

    @Override
    public ApiResult remove(Long id) {
        userRepository.remove(id);
        return ApiResult.success();
    }

    @Override
    public ApiResult update(UserDTO userDTO) {
        UserPO userPO = BeanUtil.mapperBean(userDTO, UserPO.class);
        userRepository.update(userPO);
        return ApiResult.success();
    }

    @Override
    public ApiResult find(Long id) {
        if (id < 0) {
            throw ExceptionManager.createByCodeAndMessage("USER_1003", "用户ID异常");
        }
        UserPO userPO = userRepository.find(id);
        UserDTO userDTO = BeanUtil.mapperBean(userPO, UserDTO.class);
        return ApiResult.success(userDTO);
    }

    @Override
    public ApiResult list(Integer pageNum, Integer pageSize) {
        PageResult pageResult = userRepository.list(pageNum, pageSize);
        List<UserDTO> userDTOList = BeanUtil.mapperList(pageResult.getList(), UserDTO.class);
        pageResult.setList(userDTOList);
        return ApiResult.success(pageResult);
    }
}
