package com.kimzing.test.repository.impl;

import com.kimzing.test.domain.po.UserPO;
import com.kimzing.utils.exception.ExceptionManager;
import com.kimzing.utils.page.PageResult;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 模拟仓储.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/28 16:39
 */
@Repository
public class MockUserRepository {

    private AtomicInteger idGeneration = new AtomicInteger(1);

    private static final ConcurrentHashMap<Integer, UserPO> users = new ConcurrentHashMap<>();

    public UserPO save(UserPO userPO) {
        boolean isNullId = Objects.isNull(userPO.getId());
        if (isNullId) {
            Integer userId = idGeneration.getAndIncrement();
            userPO.setId(userId);
        }

        boolean isExist = users.containsKey(userPO.getId());
        if (isExist) {
            throw ExceptionManager.createByCodeAndMessage("USER_1001", "用户信息已存在");
        }

        fillUserTimeByOperation(userPO, "CREATE");
        users.put(userPO.getId(), userPO);
        return userPO;
    }

    public void remove(Long id) {
         users.remove(id);
    }

    public void update(UserPO userPO) {
        boolean isNullId = Objects.isNull(userPO.getId());
        if (isNullId) {
            throw ExceptionManager.createByCodeAndMessage("USER_1002", "用户ID不能为空");
        }

        fillUserTimeByOperation(userPO, "UPDATE");
        users.put(userPO.getId(), userPO);
    }

    public UserPO find(Long id) {
        UserPO userPO = users.get(id);
        return userPO;
    }

    public PageResult list(Integer pageNum, Integer pageSize) {
        Collection<UserPO> values = users.values();
        List<UserPO> userPOList = values.stream().limit(pageSize).collect(Collectors.toList());
        PageResult<UserPO> userPageResult = new PageResult<>(Long.valueOf(users.size()), pageNum, pageSize, userPOList);
        return userPageResult;
    }

    public void fillUserTimeByOperation(UserPO userPO, String operationType) {
        if ("CREATE".equals(operationType)) {
            userPO.setCreateTime(LocalDateTime.now());
            userPO.setModifyTime(LocalDateTime.now());
        }
        if ("UPDATE".equals(operationType)) {
            userPO.setModifyTime(LocalDateTime.now());
        }
    }

}
