package com.kimzing.test.domain.vo;

/**
 * 性别.
 *
 * @author KimZing - kimzing@163.com
 * @since 2019/12/28 12:49
 */
public enum GenderEnum {

    MAN("先生"),WOMAN("女士"),SECRET("保密");

    private String value;

    GenderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
