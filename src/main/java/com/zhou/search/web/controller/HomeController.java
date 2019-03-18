package com.zhou.search.web.controller;

import com.zhou.search.entity.Category;
import com.zhou.search.entity.Goods;
import com.zhou.search.entity.Spu;
import com.zhou.search.mappers.CategoryMapper;
import com.zhou.search.mappers.SpuMapper;
import com.zhou.search.service.HomeService;
import com.zhou.search.utils.ClimbDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: zhou_search
 * @Auther: z_houjun
 * @Date: 2018/12/5 11:10
 * @Description:
 */
@Controller
@RequestMapping("/test")
public class HomeController {

    @Resource
    private SpuMapper spuMapper;
    @Autowired
    private HomeService service;
    @Resource
    private CategoryMapper categoryMapper;


    @RequestMapping("/home")
    public String home(){
        return "index";
    }

    @RequestMapping("/search")
    @ResponseBody
    public List<Goods> search(
            @RequestParam("keyword")String keyword,
            @RequestParam("page")int page,//当前页
            @RequestParam("size")int size //每页条数
            ){
       return service.search(keyword,page,size);
    }


    @RequestMapping("/cateGory/{name}/{id}")
    @ResponseBody
    public Spu  getData(@PathVariable("id")Long id,@PathVariable("name")String keyword){
        Spu spu = new Spu();
        spu.setId(id);
        List<Category> list = ClimbDataUtil.loadCateGory(keyword,2+"");
        for (Category c : list) {
            categoryMapper.insert(c);
        }
       return spuMapper.selectByPrimaryKey(spu);
    }


    @RequestMapping("/addData/{name}/{id}")
    @ResponseBody
    public String add(@PathVariable("id")int count,@PathVariable("name")String keyword
                      ){
        List<Category> list = categoryMapper.selectAll();
        for (int i = 1; i <= count ; i++) {
        for (Category c : list) {
                Map<String, Object> map = ClimbDataUtil.loadData(c.getName(), i*2+ "",c.getCid());
                List<Spu> data = (List<Spu>)map.get("data");
                for (int j = 0; j < data.size() ; j++) {
                    spuMapper.insert(data.get(j));
                }
            }
        }
        return "success";
    }
}
