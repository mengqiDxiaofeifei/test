package com.zhou.search.mappers;

import com.zhou.search.entity.Spu;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface SpuMapper extends Mapper<Spu> , InsertListMapper<Long> {
}
