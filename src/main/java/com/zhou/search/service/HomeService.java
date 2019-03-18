package com.zhou.search.service;

import com.zhou.search.entity.Goods;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: zhou_search
 * @Auther: z_houjun
 * @Date: 2018/12/5 11:10
 * @Description:
 */
@Service
public class HomeService {

    
    @Autowired
    private ElasticsearchTemplate template;
    
    
    public List<Goods> search(String keyword,int page,int size) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withPageable(PageRequest.of(page-1, size));
        BoolQueryBuilder baseQuery = QueryBuilders.boolQuery();
        baseQuery.must(QueryBuilders.matchQuery("all",keyword));
        queryBuilder.withQuery(baseQuery);
        AggregatedPage<Goods> pageResult = template.queryForPage(queryBuilder.build(), Goods.class);
        List<Goods> goodsList = pageResult.getContent();
        return goodsList;
    }
}
