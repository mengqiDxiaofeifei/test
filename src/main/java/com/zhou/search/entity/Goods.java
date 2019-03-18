package com.zhou.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
@Data
public class Goods {
    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    @Transient
    private String all;

    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    @Field(type = FieldType.Keyword, index = false)
    private String title;
    private Date createTime;
    private Double price;
    private String shopName;
    private String image;
}