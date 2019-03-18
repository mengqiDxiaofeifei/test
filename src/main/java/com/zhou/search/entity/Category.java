package com.zhou.search.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @program: zhou_search
 * @Auther: z_houjun
 * @Date: 2018/12/6 14:33
 * @Description:
 */
@Data
@Table(name = "t_category")
public class Category {

    @Id
    private Long cid;

    private String name;

    private String image;
}
