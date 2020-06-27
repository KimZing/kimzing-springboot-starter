package com.kimzing.test.repository;

import com.kimzing.test.domain.po.UserPO;
import com.kimzing.test.repository.impl.MockUserRepository;
import com.kimzing.utils.page.PageResult;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 模拟用户存储.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/28 11:38
 */
@Repository
public class UserRepository {

    @Resource
    MockUserRepository mockUserRepository;

    public UserPO save(UserPO userPO) {
        return mockUserRepository.save(userPO);
    }

    public void remove(Long id) {
        mockUserRepository.remove(id);
    }

    public void update(UserPO userPO) {
        mockUserRepository.update(userPO);
    }

    public UserPO find(Long id) {
        return mockUserRepository.find(id);
    }

    public PageResult<UserPO> list(Integer pageNum, Integer pageSize) {
        return mockUserRepository.list(pageNum, pageSize);
    }

}
