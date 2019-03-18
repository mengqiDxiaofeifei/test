package com.zhou.test;

import com.zhou.search.entity.Category;
import com.zhou.search.entity.Goods;
import com.zhou.search.entity.Spu;
import com.zhou.search.mappers.CategoryMapper;
import com.zhou.search.mappers.SpuMapper;
import com.zhou.search.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: zhou_search
 * @Auther: z_houjun
 * @Date: 2018/12/5 11:28
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ESTest {

    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private GoodsRepository repository;
    @Resource
    private SpuMapper spuMapper;
    @Resource
    private CategoryMapper categoryMapper;

    @Test
    public void testCreate(){
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void testClimbData(){
            List<Goods> goodsList = new ArrayList<>();
            List<Spu> spus = spuMapper.selectAll();
            for (Spu s : spus) {
                Goods goods = new Goods();
                goods.setId(s.getId());
                Category category = categoryMapper.selectByPrimaryKey(s.getCid());
                goods.setAll(category.getName()+s.getShopName()+s.getSubTitle()+s.getTitle());
                goods.setCreateTime(s.getCreateTime());
                goods.setPrice(s.getPrice());
                goods.setImage(s.getImage());
                goods.setSubTitle(s.getSubTitle());
                goods.setTitle(s.getTitle());
                goods.setShopName(s.getShopName());
                goodsList.add(goods);
            }
            repository.saveAll(goodsList);
    }



}
