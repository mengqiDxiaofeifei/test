package com.zhou.search.entity.express;


import lombok.Data;

import java.util.List;

@Data
public class Response {


    private String comCode;

    private String num;

    private List<Auto> auto;
}
