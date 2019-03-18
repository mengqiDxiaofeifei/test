package com.zhou.search.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @program: zhou_search
 * @Auther: z_houjun
 * @Date: 2018/12/5 14:07
 * @Description:
 */
@Data
@Table(name = "t_goods")
public class Spu {
    @Id
    private Long id;
    private String subTitle;
    private String title;
    private Date createTime;
    private Double price;
    private String shopName;
    private String image;
    @Transient
    private Category categroy;

    private Long cid;
}
