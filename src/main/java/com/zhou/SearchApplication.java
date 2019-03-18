package com.zhou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: zhou_search
 * @Auther: z_houjun
 * @Date: 2018/12/5 11:02
 * @Description:
 */
@SpringBootApplication
@MapperScan("com.zhou.search.mappers")
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class,args);
    }
}
