package com.zhou.search.wechat;


import com.zhou.search.entity.express.Express;
import com.zhou.search.utils.ClimbDataUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx/api")
public class ExpressController {
    @GetMapping("/getExpressInfo")
    public Express getExpressInfo(String num) {
        return ClimbDataUtil.getExpress(num);
    }
}
