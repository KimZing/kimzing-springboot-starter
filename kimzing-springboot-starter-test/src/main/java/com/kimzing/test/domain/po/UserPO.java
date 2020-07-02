package com.kimzing.test.domain.po;

import com.kimzing.test.domain.vo.GenderEnum;
import com.kimzing.utils.po.AbstractPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息实体.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/28 12:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserPO extends AbstractPO {

    private String username;

    private String password;

    private Integer age;

    private GenderEnum gender;

}
