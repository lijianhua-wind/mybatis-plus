package com.lijianhua.mybatisplus.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SexEnum {
    MALE(1, "男"),
    FEMALE(2, "女");

    @EnumValue
    private final Integer sex;
    private final String sexName;

    SexEnum(Integer sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }

    public Integer getSex() {
        return sex;
    }

    public String getSexName() {
        return sexName;
    }

    @Override
    public String toString() {
        return "SexEnum{" +
                "sex=" + sex +
                ", sexName='" + sexName + '\'' +
                '}';
    }
}
