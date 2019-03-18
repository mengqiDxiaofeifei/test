package com.zhou.search.entity.express;

import lombok.Data;

import java.util.Set;

@Data
public class Express {

    private String message;

    private String num;

    private String isCheck;

    private String condition;

    private String com;

    private String status;

    private String state;

    private Set<ExpressInfo> data;
}
