package com.zhou.search.mappers;

import com.zhou.search.entity.Category;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface CategoryMapper extends Mapper<Category>, InsertListMapper<Long> {
}
